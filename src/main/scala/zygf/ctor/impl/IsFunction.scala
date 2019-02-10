package zygf.ctor.impl

import scala.annotation.implicitNotFound

@implicitNotFound("Not a function type: ${T}")
sealed abstract class IsFunction[+T]

object IsFunction
{
  // The Egyptians did it, so can I
  implicit object Function0 extends IsFunction[() => Nothing]
  implicit object Function1 extends IsFunction[Any => Nothing]
  implicit object Function2 extends IsFunction[(Any, Any) => Nothing]
  implicit object Function3 extends IsFunction[(Any, Any, Any) => Nothing]
  implicit object Function4 extends IsFunction[(Any, Any, Any, Any) => Nothing]
  implicit object Function5 extends IsFunction[(Any, Any, Any, Any, Any) => Nothing]
  implicit object Function6 extends IsFunction[(Any, Any, Any, Any, Any, Any) => Nothing]
  implicit object Function7 extends IsFunction[(Any, Any, Any, Any, Any, Any, Any) => Nothing]
  implicit object Function8 extends IsFunction[(Any, Any, Any, Any, Any, Any, Any, Any) => Nothing]
  implicit object Function9 extends IsFunction[(Any, Any, Any, Any, Any, Any, Any, Any, Any) => Nothing]
  implicit object Function10 extends IsFunction[(Any, Any, Any, Any, Any, Any, Any, Any, Any, Any) => Nothing]
  implicit object Function11 extends IsFunction[(Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any) => Nothing]
  implicit object Function12 extends IsFunction[(Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any) => Nothing]
  implicit object Function13 extends IsFunction[(Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any) => Nothing]
  implicit object Function14 extends IsFunction[(Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any) => Nothing]
  implicit object Function15 extends IsFunction[(Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any) => Nothing]
  implicit object Function16 extends IsFunction[(Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any) => Nothing]
  implicit object Function17 extends IsFunction[(Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any) => Nothing]
  implicit object Function18 extends IsFunction[(Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any) => Nothing]
  implicit object Function19 extends IsFunction[(Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any) => Nothing]
  implicit object Function20 extends IsFunction[(Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any) => Nothing]
  implicit object Function21 extends IsFunction[(Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any) => Nothing]
  implicit object Function22 extends IsFunction[(Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any) => Nothing]
}
