package org.scalajs

import reflect.ClassManifest
import org.mozilla.javascript.{Scriptable, Context, Callable}

class Conversions(ctx: Context, scope: Scriptable) {
  def unwrap[T](v: AnyRef)(implicit mf: Manifest[T]): T = {

    val argClasses = mf.typeArguments.map(_.erasure)
    def call(c: Callable, args: Array[AnyRef]) = {
      val ret = c.call(ctx, scope, null, args)
      Context.jsToJava(ret, argClasses.last)
    }

    v match {
      case c: Callable => {
        val cf0 = classOf[Function0[_]]
        val cf1 = classOf[Function1[_,_]]
        val cf2 = classOf[Function2[_,_,_]]

        mf.erasure match {
          case v if v == cf0 => (() => call(c, new Array(0))).asInstanceOf[T]
          case v if v == cf1 => ((a:AnyRef) => call(c, Array[AnyRef](a))).asInstanceOf[T]
          case v if v == cf2 => ((a:AnyRef,b:AnyRef) => call(c, Array[AnyRef](a, b))).asInstanceOf[T]
          case _ => throw new UnsupportedOperationException()
        }
      }
      case _ => throw new UnsupportedOperationException()
    }
  }
}
