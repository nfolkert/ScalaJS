package org.scalajs.utils

import java.lang.reflect.{Field, Method, Constructor}

object ReflectionUtils {
  def getAllConstructors[T](cls:Class[T]): List[Constructor[T]] = {
    cls.getDeclaredConstructors().asInstanceOf[Array[Constructor[T]]].toList
  }

  def getAllMethods[T](cls:Class[T]): List[Method] = {
    cls.getDeclaredMethods().toList ++ (if (cls.getSuperclass() != null) getAllMethods(cls.getSuperclass()) else Nil)
  }

  def getAllFields[T](cls:Class[T]): List[Field] = {
    cls.getDeclaredFields().toList ++ (if (cls.getSuperclass() != null) getAllFields(cls.getSuperclass()) else Nil)
  }

  def getShortName[T](cls:Class[T]): String = {
    cls.getName() // TODO
  }
}