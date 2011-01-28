package org.scalajs

import utils.ReflectionUtils
import java.lang.{Class, String}
import org.mozilla.javascript._
import java.lang.reflect.{Field, Method}

class JSComponents {}

/**
 * TODO: support overloaded constructors?
 */
class JSScalaConstructor(cls:Class[AnyRef]) extends ScriptableObject with Function {
  val constructor = {
    ReflectionUtils.getAllConstructors(cls).filter((c)=>{c.getAnnotation[JSClass](classOf[JSClass]) != null})(0)
  }
 
  lazy val clsName = ReflectionUtils.getShortName(cls)
  def getClassName = clsName

  def construct(cx: Context, scope: Scriptable, args: Array[AnyRef]) = {
    val obj = constructor.newInstance(args)
    new JSScalaObject(obj);
  }

  def call(cx: Context, scope: Scriptable, thisObj: Scriptable, args: Array[AnyRef]) = throw new UnsupportedOperationException();
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

  override def put(name: String, start: Scriptable, value: AnyRef) =
  {
    val inSlot = get(name, start)
    inSlot match {
      case f:JSScalaField => f.field.set(f.obj, ArgumentConvertor.jsToScala(value)) // TODO: make field accessible
      case _ => super.put(name, start, value)
    }
  }

  override def get(name: String, start: Scriptable) = {
    val inSlot = get(name, start)
    inSlot match {
      case f:JSScalaField => f.field.get(f.obj) // TODO: cache the intermediate results, wrap
      case _ => super.get(name, start)
    }
  }
}

/**
 * TODO: support overloaded methods?
 */
class JSScalaMethod(obj:AnyRef, method:Method) extends ScriptableObject with Function {
  def getClassName = ""

  def construct(cx: Context, scope: Scriptable, args: Array[AnyRef]) = throw new UnsupportedOperationException();

  def call(cx: Context, scope: Scriptable, thisObj: Scriptable, args: Array[AnyRef]) = {
    ArgumentConvertor.scalaToJs(method.invoke(obj, ArgumentConvertor.jsToScala(args)))
  }
}

class JSScalaField(val obj:AnyRef, val field:Field) {}

object ArgumentConvertor {
  def jsToScala(args: Array[AnyRef]): Array[AnyRef] = {args}
  def jsToScala(arg:AnyRef): AnyRef = {arg}

  def scalaToJs(args: Array[AnyRef]): Array[AnyRef] = {args}
  def scalaToJs(arg: AnyRef): AnyRef = {arg}
}