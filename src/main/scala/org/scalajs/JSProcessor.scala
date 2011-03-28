package org.scalajs

import org.mozilla.javascript.{Script, ScriptableObject, ContextFactory, Context}

class JSProcessor {

}

object JSProcessor extends JSProcessor {

  def compileScript(script: String, desc: String, lineNo: Int) {
    val ctx: Context = new ContextFactory().enterContext()
    try {
      val compiledScript: Script = ctx.compileString(script, desc, lineNo, null)
      compiledScript
    } finally {
      Context.exit
    }
  }

  def setupStandardScope() = {
    val ctx: Context = new ContextFactory().enterContext()
    try {
      val scope: ScriptableObject = ctx.initStandardObjects()
      // scope.putConst()
      scope
    } finally {
      Context.exit
    }
  }

  def executeCompiledScript(compiledScript: Script) {
    val ctx: Context = new ContextFactory().enterContext()
    try {
      val res = compiledScript.exec(ctx, setupStandardScope)
      res
    } finally {
      Context.exit
    }
  }

  def executeScriptString(script: String, desc: String, lineNo: Int) {
    val ctx: Context = new ContextFactory().enterContext()
    try {
      val res = ctx.evaluateString(setupStandardScope(), script, desc, lineNo, null)
      res
    } finally {
      Context.exit
    }
  }
}
