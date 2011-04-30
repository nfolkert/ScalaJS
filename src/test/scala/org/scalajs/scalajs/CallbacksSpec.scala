package org.scalajs.scalajs

import org.specs.Specification
import org.mozilla.javascript.{Callable, ContextFactory, ScriptableObject, Context}
package callbacksspec {

}

object CallbacksSpec extends Specification("Callbacks Specification") {
  import callbacksspec._

  "Callbacks" should {

    "return a callback that can be applied in an enclosing scala context" in {
      val ctx:Context = new ContextFactory().enterContext()
      val scope:ScriptableObject = ctx.initStandardObjects()

      val cbkObj = ctx.evaluateString(scope, "var x = function(a) {return a + 1}; x", "Callbacks", 1, null)

      val cbk = cbkObj match {
        case c: Callable => (x:Int) => {c.call(ctx, scope, null, Array(x.asInstanceOf[AnyRef]))}
      }

      List(1, 2, 3).map(cbk) must_== List(2, 3, 4)
    }
  }  
}
