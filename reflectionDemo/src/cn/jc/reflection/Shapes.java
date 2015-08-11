package cn.jc.reflection;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class Shapes {
	
	/**
	 * 测试继承关系
	 * RTTI: Run-Time Type Information，在运行时，识别一个对象的类型
	 */
	@Test
	public void testInherit() {
		List<Shape> shapeList = Arrays.asList(new Circle(), new Square(), new Triangle());
		for (Shape shape : shapeList) {
			//Shape对象实际执行什么样的代码，是由引用所指向的具体对象Circle/Square/Triangle决定的
			shape.draw();
		}
	}
	
}

abstract class Shape{
	void draw(){
		//如果某个对象出现在字符串表达式中，toString()方法就会被自动调用
		System.out.println(this + ".draw()");
	}
	
	/**
	 * abstract强制继承者覆写该方法
	 */
	abstract public String toString();
}

class Circle extends Shape{
	@Override
	public String toString() {
		return "Circle";
	}	
}

class Square extends Shape{

	@Override
	public String toString() {
		return "Square";
	}
	
}

class Triangle extends Shape{

	@Override
	public String toString() {
		return "Triangle";
	}	
}
