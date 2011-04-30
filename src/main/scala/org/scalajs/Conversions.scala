package org.scalajs

import reflect.ClassManifest
import org.mozilla.javascript.{Scriptable, Context, Callable}

class Conversions(ctx: Context, scope: Scriptable) {

  private def unwrapBasic(v: AnyRef, mf: Manifest[_]): AnyRef = {
    Context.jsToJava(v, mf.erasure)
  }

  def unwrap[T](v: AnyRef)(implicit mf: Manifest[T]): T = {
    def call(c: Callable, args: Array[AnyRef]): AnyRef = {
      val ret = c.call(ctx, scope, null, args)
      unwrapBasic(ret, mf.typeArguments.last)
    }

    v match {
      case fn: Callable => {
        val cf0 = classOf[Function0[_]]
        val cf1 = classOf[Function1[_,_]]
        val cf2 = classOf[Function2[_,_,_]]
        val cf3 = classOf[Function3[_,_,_,_]]
        val cf4 = classOf[Function4[_,_,_,_,_]]
        val cf5 = classOf[Function5[_,_,_,_,_,_]]
        val cf6 = classOf[Function6[_,_,_,_,_,_,_]]

        type A = AnyRef
        mf.erasure match {
          case v if v == cf0 => (() => call(fn, new Array(0))).asInstanceOf[T]
          case v if v == cf1 => ((a:A) => call(fn, Array[A](a))).asInstanceOf[T]
          case v if v == cf2 => ((a:A,b:A) => call(fn, Array[A](a,b))).asInstanceOf[T]
          case v if v == cf3 => ((a:A,b:A,c:A) => call(fn, Array[A](a,b,c))).asInstanceOf[T]
          case v if v == cf4 => ((a:A,b:A,c:A,d:A) => call(fn, Array[A](a,b,c,d))).asInstanceOf[T]
          case v if v == cf5 => ((a:A,b:A,c:A,d:A,e:A) => call(fn, Array[A](a,b,c,d,e))).asInstanceOf[T]
          case v if v == cf6 => ((a:A,b:A,c:A,d:A,e:A,f:A) => call(fn, Array[A](a,b,c,d,e,f))).asInstanceOf[T]

          case _ => throw new UnsupportedOperationException("Currently only functions up to 6 arguments are supported")
        }
      }
      case obj => unwrapBasic(obj, mf).asInstanceOf[T]
    }
  }

  // TODO: unwrap to JSON objects; unwrap to collections (use manifest for component conversion)
  // TODO: better way to user manifest in the functions?
  // TODO: wrap method that will convert JSON objects appropriately
}
