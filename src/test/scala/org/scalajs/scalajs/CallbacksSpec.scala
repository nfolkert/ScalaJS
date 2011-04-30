package org.scalajs.scalajs

import org.specs.Specification
import org.mozilla.javascript.{Callable, ContextFactory, ScriptableObject, Context}
import org.scalajs.Conversions
import reflect.Manifest
package callbacksspec {

}

object CallbacksSpec extends Specification("Callbacks Specification") {
  import callbacksspec._

  "Callbacks" should {

    "return a callback that can be applied in an enclosing scala context" in {
      val ctx:Context = new ContextFactory().enterContext()
      val scope:ScriptableObject = ctx.initStandardObjects()
      val conversion = new Conversions(ctx, scope)

      def eval(s:String) = ctx.evaluateString(scope, s, "Callbacks", 1, null)

      val cbk0  = conversion.unwrap[()=>Int](eval("var x = function() {return 0}; x"))
      val cbk1  = conversion.unwrap[Int=>Int](eval("var x = function(a) {return a + 1}; x"))
      val cbk2 = conversion.unwrap[(Int,Int)=>Int](eval("var x = function(a, b) {return a + b}; x"))

      List(1, 2, 3).map((a) => cbk0()) must_== List(0, 0, 0)
      List(1, 2, 3).map(cbk1) must_== List(2, 3, 4)
      List((1,2), (2,3), (3,4)).map(cbk2.tupled) must_== List(3, 5, 7)
    }
  }
}
