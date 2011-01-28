package org.scalajs.utils

import org.specs.SpecsMatchers
import org.junit.Test

class ReflectionUtilsTest extends SpecsMatchers {

  class TestClass(in:List[String]) {
    def this(a: String, b: String) = this(a :: List(b))
  }

  @Test
  def testConstructors(): Unit = {
    val allCons = ReflectionUtils.getAllConstructors(classOf[TestClass])
    allCons.length must_== 2
  }
}
