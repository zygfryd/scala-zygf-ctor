package zygf.ctor

import scala.reflect.macros.{TypecheckException, blackbox}

import zygf.ctor.impl.IsFunction

final class Ctor[F] private(val make: F) extends AnyVal
{
  override def toString: String = s"Ctor(${ make })"
}

object Ctor
{
  def get[F: IsFunction](implicit ctor: Ctor[F]): Ctor[F] = ctor
  
  def apply[F: IsFunction](make: F) = new Ctor(make)
  
  implicit def gen[F]: Ctor[F] = macro impl[F]
  
  def impl[F: c.WeakTypeTag](c: blackbox.Context): c.Tree = {
    import c.universe._
    
    val F = implicitly[c.WeakTypeTag[F]]
    
    case class Uncurried(returnType:  c.Type,
                         formalLists: List[List[c.universe.ValDef]] = Nil,
                         actualLists: List[List[c.Tree]] = Nil)
    
    // analyze a curried function type and return: the final result type and argument lists for building a lambda
    def uncurry(resultType: c.Type): Uncurried = {
      resultType.dealias match {
        case TypeRef(_, sym, args) if sym.fullName.startsWith("scala.Function") =>
          val argTypes = args.toVector
          
          val namesAndTypes = argTypes.dropRight(1).map { t => (t, TermName(c.freshName())) }.toList
          
          val formal = namesAndTypes.map {
            case (t, name) =>
              ValDef(NoMods, name, Ident(t.typeSymbol), c.universe.EmptyTree)
          }
          
          val actual = namesAndTypes.map {
            case (_, name) =>
              Ident(name)
          }
          
          val inner = uncurry(args.last)
          
          Uncurried(returnType  = inner.returnType,
                    formalLists = formal :: inner.formalLists,
                    actualLists = actual :: inner.actualLists)
          
        case other =>
          Uncurried(other)
      }
    }
    
    def buildApply(actuals: List[List[c.Tree]], target: c.Tree): c.Tree = {
      // ((target 1) 2) 3
      actuals.tail match {
        case Nil =>
          Apply(target, actuals.head)
        case tail =>
          Apply(buildApply(tail, target), actuals.head)
      }
    }
    
    def buildLambda(formals: List[List[c.universe.ValDef]], target: c.Tree): c.Tree = {
      // 1 => 2 => 3 => ((target 1) 2) 3
      formals.tail match {
        case Nil =>
          Function(formals.head, target)
        case tail =>
          Function(formals.head, buildLambda(tail, target))
      }
    }
    
    val Uncurried(rtAlias, formalLists, actualLists) = uncurry(F.tpe)
    
    if (formalLists.isEmpty) {
      c.error(c.enclosingPosition, s"Not a function type: ${rtAlias}")
      return q""
    }
    
    val self = symbolOf[Ctor[_]].companion
    
    val returnType = rtAlias.dealias
    lazy val rtName = if (returnType eq rtAlias) returnType.toString else s"${rtAlias} (resolved to: ${returnType})"
    
    // if there's a companion, try its .apply method first
    returnType.typeSymbol.companion match {
      case NoSymbol =>
      case companion =>
        val apply = Typed(buildApply(actualLists.reverse, Select(Ident(companion), TermName("apply"))), Ident(returnType.typeSymbol))
        val lambda = buildLambda(formalLists, apply)
        val construct = q"""($self.apply { $lambda })"""
        //val construct = q"""($self.apply { $companion.apply: ${F} })""" // you'd be tempted to try eta expansion, but it won't work with implicit params
        
        try {
          return c.typecheck(construct)
        }
        catch {
          case _: TypecheckException =>
            // .apply didn't compile, let's just move on to the constructor
        }
    }
    
    // if a concrete type, try its constructor
    if (!returnType.typeSymbol.isAbstract) {
      val `new` = buildApply(actualLists.reverse, Select(New(Ident(returnType.typeSymbol)), termNames.CONSTRUCTOR))
      val lambda = buildLambda(formalLists, `new`)
      val construct = q"""($self.apply { $lambda })"""
      
      try {
        return c.typecheck(construct)
      }
      catch {
        case e: TypecheckException =>
          c.echo(c.enclosingPosition, e.msg)
          c.abort(c.enclosingPosition, s"No accessible constructor for ${rtName} that matches: ${F.tpe}")
      }
    }
    else {
      c.abort(c.enclosingPosition, s"Type is abstract, cannot construct: ${rtName}")
    }
  }
}
