package org.scalajs.scalajs

import org.specs.Specification
import org.mozilla.javascript.{Callable, ContextFactory, ScriptableObject, Context}
import org.scalajs.Conversions
import reflect.Manifest
package callbacksspec {

}

object ConversionsSpec extends Specification("Conversions Specification") {
  import callbacksspec._

  "Conversions" should {

    "return a callback that can be applied in an enclosing scala context" in {
      val ctx:Context = new ContextFactory().enterContext()
      val scope:ScriptableObject = ctx.initStandardObjects()
      val conversion = new Conversions(ctx, scope)

      def eval(s:String) = ctx.evaluateString(scope, s, "Callbacks", 1, null)

      val obj = conversion.unwrap[Int](eval("var x = 5; x")) must_== 5
      val cbk0  = conversion.unwrap[()=>Int](eval("var x = function() {return 0}; x"))
      val cbk1  = conversion.unwrap[Int=>Int](eval("var x = function(a) {return a + 1}; x"))
      val cbk2 = conversion.unwrap[(Int,Int)=>Int](eval("var x = function(a, b) {return a + b}; x"))
      val cbk3 = conversion.unwrap[(Int,Int,Int)=>Int](eval("var x = function(a, b, c) {return a + b + c}; x"))
      val cbk4 = conversion.unwrap[(Int,Int,Int,Int)=>Int](eval("var x = function(a, b, c, d) {return a + b + c + d}; x"))
      val cbk5 = conversion.unwrap[(Int,Int,Int,Int,Int)=>Int](eval("var x = function(a, b, c, d, e) {return a + b + c + d + e}; x"))
      val cbk6 = conversion.unwrap[(Int,Int,Int,Int,Int,Int)=>Int](eval("var x = function(a, b, c, d, e, f) {return a + b + c + d + e + f}; x"))

      List(1, 2, 3).map((a) => cbk0()) must_== List(0, 0, 0)
      List(1, 2, 3).map(cbk1) must_== List(2, 3, 4)
      List((1,2), (2,3), (3,4)).map(cbk2.tupled) must_== List(3, 5, 7)
      List((1,2,3), (2,3,4), (3,4,5)).map(cbk3.tupled) must_== List(6, 9, 12)
      List((1,2,3,4), (2,3,4,5), (3,4,5,6)).map(cbk4.tupled) must_== List(10, 14, 18)
      List((1,2,3,4,5), (2,3,4,5,6), (3,4,5,6,7)).map(cbk5.tupled) must_== List(15, 20, 25)
      List((1,2,3,4,5,6), (2,3,4,5,6,7), (3,4,5,6,7,8)).map(cbk6.tupled) must_== List(21, 27, 33)
    }
  }
}
