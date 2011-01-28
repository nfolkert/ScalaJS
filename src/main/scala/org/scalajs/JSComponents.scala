package org.scalajs

import utils.ReflectionUtils
import java.lang.{Class, String}
import org.mozilla.javascript._
import java.lang.reflect.{Field, Method}

class JSComponents {}

/**
 * A Scala constructor
 */
class JSScalaConstructor(cls:Class[AnyRef]) extends ScriptableObject with Function {
  val constructor = {
    // how do we pick out the correct constructor?
    val constructors = ReflectionUtils.getAllConstructors(cls).filter((c)=>{
      c.getAnnotation[JSClass](classOf[JSClass]) != null
    })
    if (constructors.length != 1) throw new IllegalArgumentException(constructors.length + " enabled constructors were found (required: 1)")
    constructors(0)
  }
 
  lazy val clsName = ReflectionUtils.getShortName(cls)
  def getClassName = clsName

  def construct(cx: Context, scope: Scriptable, args: Array[AnyRef]) = {
    val obj = constructor.newInstance(args)
    new JSScalaObject(obj);
  }

  def call(cx: Context, scope: Scriptable, thisObj: Scriptable, args: Array[AnyRef]) = null
}

/**
 * A Scala object
 */
class JSScalaObject(val obj:AnyRef) extends ScriptableObject with Wrapper {
  {
    val methods = ReflectionUtils.getAllMethods(obj.getClass())
    methods.map((m)=>if (m.getAnnotation[JSMethod](classOf[JSMethod]) != null) put(m.getName(), this, new JSScalaMethod(obj, m)))

    val fields = ReflectionUtils.getAllFields(obj.getClass)
    fields.map((f)=>if (f.getAnnotation[JSProperty](classOf[JSProperty]) != null) put(f.getName(), this, new JSScalaField(obj, f)))
  }

  def unwrap = obj
  def getClassName = ""
}

/**
 *
 */
class JSScalaMethod(obj:AnyRef, method:Method) extends ScriptableObject with Function {
  def getClassName = ""

  def construct(cx: Context, scope: Scriptable, args: Array[AnyRef]) = null

  def call(cx: Context, scope: Scriptable, thisObj: Scriptable, args: Array[AnyRef]) = {
    null
  }
}

class JSScalaField(val obj:AnyRef, val field:Field) extends ScriptableObject {
  def getClassName = ""
  // TODO
}

