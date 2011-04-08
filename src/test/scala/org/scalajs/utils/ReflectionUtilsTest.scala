package org.scalajs.utils

import org.specs.SpecsMatchers
import org.junit.Test

class ReflectionUtilsTest extends SpecsMatchers {

  @Test
  def testConstructors(): Unit = {
    val allCons = ReflectionUtils.getAllConstructors(classOf[TestClass])
    allCons.length must_== 2

    val innerAllCons = ReflectionUtils.getAllConstructors(classOf[InnerTestClass])
    innerAllCons.length must_== 2

  }

  @Test
  def testMethods(): Unit = {
    val allMethods = ReflectionUtils.getAllMethods(classOf[TestClass], false)
    allMethods.length must_== 10 // All the fields have implicit methods

    val innerAllMethods = ReflectionUtils.getAllMethods(classOf[InnerTestClass], false)
    innerAllMethods.length must_==  11 // adds outer field/method
  }

  @Test
  def testFields(): Unit = {
    val allFields = ReflectionUtils.getAllFields(classOf[TestClass])
    allFields.length must_== 6

    val innerAllFields = ReflectionUtils.getAllFields(classOf[InnerTestClass])
    innerAllFields.length must_== 7 // adds outer field
  }

  class InnerTestClass(in:List[String]) {
    def this(a: String, b: String) = this(a :: List(b))

    def callProcedure(fn:()=>Unit):Unit = fn()

    val x = 5
    def addFive(v: Int): Int = v + x

    def fnFn(desc: String)(block: => Any) = {println(desc); (block)}

    val yFn = fnFn _
    val yFn2 = fnFn("meep") _

    def fn2(a: Int, b: Int) = a + b
    val fn2curried = (fn2 _).curried
    val yFnx = fn2 _
    val yFnx2 = fn2curried(3)
  }
}

class TestClass(in:List[String]) {
  def this(a: String, b: String) = this(a :: List(b))

  def callProcedure(fn:()=>Unit):Unit = fn()

  val x = 5
  def addFive(v: Int): Int = v + x

  def fnFn(desc: String)(block: => Any) = {println(desc); (block)}

  val yFn = fnFn _
  val yFn2 = fnFn("meep") _

  def fn2(a: Int, b: Int) = a + b
  val fn2curried = (fn2 _).curried
  val yFnx = fn2 _
  val yFnx2 = fn2curried(3)
}
