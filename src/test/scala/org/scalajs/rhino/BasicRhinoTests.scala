package org.scalajs.rhino

import org.specs.SpecsMatchers
import org.junit.Test
import org.mozilla.javascript.{ScriptableObject, Context, ContextFactory}

class BasicRhinoTests extends SpecsMatchers {

  @Test
  def testRunningAScript: Unit = {
    while (Context.getCurrentContext != null) Context.exit

    val ctx:Context = new ContextFactory().enterContext()
    val scope:ScriptableObject = ctx.initStandardObjects()

    ctx.evaluateString(scope, "'hello world'", "BasicRhinoTests.testRunningAScript", 1, null) must_== "hello world"
    ctx.evaluateString(scope, "5", "BasicRhinoTests.testRunningAScript", 1, null) must_== 5
    ctx.evaluateString(scope, "5 * 3", "BasicRhinoTests.testRunningAScript", 1, null) must_== 15
    ctx.evaluateString(scope, "var x = 5 * 3; x", "BasicRhinoTests.testRunningAScript", 1, null) must_== 15

    Context.exit()
  }
}
