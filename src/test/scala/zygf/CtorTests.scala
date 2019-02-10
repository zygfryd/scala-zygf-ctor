package zygf

import zygf.ctor._

class CtorTests extends org.scalatest.FunSuite
{
  trait Abstract
  
  class Private private ()
  
  class Args0
  
  trait HasApply1
  
  object HasApply1
  {
    def apply(s: CharSequence) = new HasApply1 {}
  }
  
  test("expected failures") {
    assertDoesNotCompile("implicitly[Ctor[Any => Args0]]")
    assertDoesNotCompile("implicitly[Ctor[Any => Private]]")
    assertDoesNotCompile("implicitly[Ctor[Any => Abstract]]")
    assertDoesNotCompile("implicitly[Ctor[Any => HasApply1]]")
  }
  
  test("covariance") {
    assert(implicitly[Ctor[CharSequence => HasApply1]].make("").isInstanceOf[HasApply1])
    assert(implicitly[Ctor[String => HasApply1]].make("").isInstanceOf[HasApply1])
  }
  
  case class Args2(i: Int, s: String)
  
  class Args1and1(i: Int)(implicit s: String)
  
  test("multiple args") {
    assertDoesNotCompile("implicitly[Ctor[Int => String => Args2]]")
    assert(implicitly[Ctor[(Int, String) => Args2]].make(0, "").isInstanceOf[Args2])
  }
  
  test("curried") {
    assertDoesNotCompile("implicitly[Ctor[(Int, String) => Args1and1]]")
    assert(implicitly[Ctor[Int => String => Args1and1]].make(0)("").isInstanceOf[Args1and1])
  }
  
  test("Ctor.get") {
    assert(Ctor.get[Int => String => Args1and1].make(0)("").isInstanceOf[Args1and1])
  }
  
  test("Ctor.get is transitive") {
    def foo[F: impl.IsFunction: Ctor] = Ctor.get[F]
    assert(foo[Int => String => Args1and1].make(0)("").isInstanceOf[Args1and1])
  }
  
  test("Ctor.gen") {
    assert(Ctor.gen[Int => String => Args1and1].make(0)("").isInstanceOf[Args1and1])
  }
  
  test("Ctor.gen isn't transitive") {
    assertDoesNotCompile("def foo[F: impl.IsFunction: Ctor] = Ctor.gen[F]")
  }
}
