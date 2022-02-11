package icse.demo.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Class demonstrating the use of the Reflection API in Java. Based on
 * https://docs.oracle.com/javase/tutorial/reflect/member/index.html and
 * https://docs.oracle.com/javase/tutorial/reflect/class/index.html
 */
public class JavaReflectionDemo
{

	/**
	 * This main method demonstrates how to use the Reflection API to query information about classes and their members,
	 * change field values, and invoke methods and constructors, all at runtime.
	 * 
	 * @param args
	 *            command line arguments (not used)
	 */
	public static void main(String[] args)
	{
		List<String> authors = new ArrayList<>();
		authors.add("Robert Jordan");
		/*?
		 * Keyword: Book:1
		 * This class is declared at the end of this file.
		 */
		Book book = new Book("A Memory of Light", authors);

		queryClassInformation("icse.demo.reflection.Book");
		getAndChangeField(book);
		invokeMethod(book);
		invokeConstructor();
	}

	/**
	 * Shows information about an unknown target class.
	 * 
	 * If the argument is a String, it is expected to be the fully qualified name of the class (e.g.,
	 * "icse.demo.reflection.Book"). If the argument is a Class object, the method shows information about that class.
	 * If it is an instance of any other class, the method shows information about the run-time class of that object.
	 * 
	 * @param unknown
	 *            the target class.
	 */
	public static void queryClassInformation(Object unknown)
	{
		/*?
		 * Keyword: Class<?>
		 * To get information about a Java class (or interface), the first step is to get a reference to
		 * a `Class` object representing that class (or interface).
		 * 
		 * There are 3 ways to get a reference to a `Class` object:
		 * 1. by parsing a `String` that contains the _fully qualified name_ of the class with `Class.forName(String)`,
		 * 2. by using a _class literal_,
		 * 3. by calling `getClass()` on an instance of that class.
		 * 
		 * Internal: fully qualified name
		 * Class<?>
		 * The unique name of the class, including the package name.
		 * 
		 * For example:
		 * - `icse.demo.reflection.Book`
		 * - `java.lang.String`
		 * - `java.util.Map`
		 * - `java.util.Map$Entry` (`Entry` is a nested class that is a member of the `Map` class)
		 * 
		 * Internal: class literal
		 * Class<?>
		 * _Literal_ expressions directly defines constant values in Java. For example, an
		 * `int` literal can be `123` and a `String` literal can be `"hello"`.
		 * 
		 * A _class_ literal defines a value of type `Class`. It takes the form of the class
		 * name followed by `.class`. For example, `String.class` or `Book.class`.
		 * 
		 * Internal: instance of that class
		 * Class<?>
		 * `getClass()` will return the exact run-time class of the object on which it
		 * is called, rather than a parent class (or interface). For example, in the following code
		 * ```java
		 * List<String> x = new ArrayList<>();
		 * Class<?> actualClass = x.getClass();
		 * ```
		 * the variable `actualClass` represents the class `ArrayList`, even though the
		 * variable `x` has the type `List` (which is a supertype of `ArrayList`).
		 */
		Class<?> unknownClass;
		if (unknown instanceof String)
		{
			/*?
			 * Block: 9
			 * Class not found
			 * The method `Class.forName(String)` throws a `ClassNotFoundException` if
			 * there is no class with the given fully qualified name. This can happen if
			 * there is a typo in the name, the simple name is used (without the package),
			 * dots (`.`) are used instead of dollar signs (`$`) to indicate nested classes, etc.
			 * 
			 * Internal: fully qualified name
			 * 9
			 * The unique name of the class, including the package name.
			 * 
			 * For example:
			 * - `icse.demo.reflection.Book`
			 * - `java.lang.String`
			 * - `java.util.Map`
			 * - `java.util.Map$Entry` (`Entry` is a nested class that is a member of the `Map` class)
			 */
			try
			{
				unknownClass = Class.forName((String) unknown);
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
				return;
			}
		}
		else if (unknown instanceof Class)
		{
			unknownClass = (Class<?>) unknown;
		}
		else
		{
			/*?
			 * Keyword: getClass
			 * `getClass()` will return the exact run-time class of the object on which it
			 * is called, rather than a parent class (or interface). For example, in the following code
			 * ```java
			 * List<String> x = new ArrayList<>();
			 * Class<?> actualClass = x.getClass();
			 * ```
			 * the variable `actualClass` represents the class `ArrayList`, even though the
			 * variable `x` has the type `List` (which is a supertype of `ArrayList`).
			 */
			unknownClass = unknown.getClass();
		}
		System.out.println("Information about class: " + unknownClass.getName());

		System.out.println("Fields:");
		/*?
		 * Keyword: getDeclaredFields()
		 * This method returns all fields _declared in this class_. It doesn't include
		 * inherited fields, but it includes non public ones.
		 * 
		 * To also get fields from parent classes, use `getFields()` instead. However, this method
		 * only returns _public_ fields.
		 * 
		 * Internal: inherited fields
		 * getDeclaredFields()
		 * fields declared in parent classes
		 */
		for (Field field : unknownClass.getDeclaredFields())
		{
			/*?
			 * Keyword: toGenericString()
			 * This method returns information about the field in a String, most likely to
			 * be showed as is to humans.
			 * 
			 * To get specific information, there are methods like `getName()`, `getModifiers()`,
			 * and `getType()`.
			 */
			System.out.println("  " + field.toGenericString());
		}

		System.out.println("Methods:");
		/*?
		 * Keyword: getDeclaredMethods()
		 * This method returns all methods _declared in this class_. It doesn't include
		 * inherited methods, but it includes non public ones.
		 * 
		 * For this API, a constructor **is not** a method, and therefore `getDeclaredMethods()`
		 * will not return constructors.
		 * 
		 * To also get methods from parent classes, use `getMethods()` instead. However, this method
		 * only returns _public_ methods.
		 * 
		 * Internal: inherited methods
		 * getDeclaredMethods()
		 * methods declared in parent classes
		 * 
		 * Internal: will not return constructors
		 * getDeclaredMethods()
		 * to get those, call `getDeclaredConstructors()`
		 */
		for (Method method : unknownClass.getDeclaredMethods())
		{
			/*?
			 * Keyword: toGenericString()
			 * This method returns information about the method in a String, most likely to
			 * be showed as is to humans.
			 * 
			 * To get specific information, there are methods like `getName()`, `getModifiers()`,
			 * and `getType()`.
			 */
			System.out.println("  " + method.toGenericString());
		}

		System.out.println("Constructors:");
		/*?
		 * Keyword: getDeclaredConstructors()
		 * This method returns all constructors (public or not) for this class.
		 * 
		 * Internal: all constructors
		 * getDeclaredConstructors()
		 * Contrary to fields and methods, constructors are not inherited from
		 * parent classes (according to the Reflection API).
		 */
		for (Constructor<?> constructor : unknownClass.getDeclaredConstructors())
		{
			/*?
			 * Keyword: toGenericString()
			 * This method returns information about the field in a String, most likely to
			 * be showed as is to humans.
			 * 
			 * To get specific information, there are methods like `getName()`, `getModifiers()`,
			 * and `getType()`.
			 */
			System.out.println("  " + constructor.toGenericString());
		}
		System.out.println();
	}

	/**
	 * Accesses and modifies the private fields (title and authors) of a book using reflection.
	 * 
	 * @param book
	 *            the book object to modify
	 */
	public static void getAndChangeField(Book book)
	{
		try
		{
			/*?
			 * Keyword: bookClass
			 * To get access to fields of an object `X`, the first thing to do is get a reference
			 * to a `Class` object that represents the class of `X`. Here, we get a reference to
			 * the class `Book` using a class literal (`Book.class`).
			 * 
			 * Keyword: Book.class
			 * This is a class literal: an expression that encodes a constant value of type `Class`,
			 * similar to how the _string_ literal `"hello"` encodes a constant value of type String.
			 * 
			 * Here, the class literal represents the class `Book`.
			 */
			Class<?> bookClass = Book.class;
			/*?
			 * Keyword: Field titleField
			 * To manipulate fields of an object (or static fields of a class), we get a reference
			 * to an object of class `Field`. We can get this reference using the method `getDeclaredField()`
			 * _on the `Class` object_.
			 * 
			 * Note that the object that we want to modify (i.e., the actual book object)
			 * isn't needed.
			 */
			Field titleField = bookClass.getDeclaredField("title");
			/*?
			 * Keyword: setAccessible
			 * The field `title` of class `Book` is private, meaning that it can't be accessed or
			 * modified outside the class `Book` (i.e., here). To make it possible to change the
			 * title, we need to call `setAccessible(true)` on the field object.
			 */
			titleField.setAccessible(true);
			/*?
			 * Keyword: set
			 * The method `set` is used to change the value of a field.
			 * 
			 * Keyword: book
			 * When calling `set`, the first argument must be the object for which we need to change
			 * the field value. Here, it's the `book` variable. This argument is required because up
			 * to this point, the `book` variable isn't involved, meaning that the previous code would
			 * be the same to change the title of _any book object_.
			 * 
			 * If the field to change is a static field, the first argument isn't used, so we can use
			 * `null` (e.g., `staticField.set(null, newValue)`).
			 * 
			 * The second argument is the new value.
			 */
			titleField.set(book, "The Wheel of Time: A Memory of Light");

			/*?
			 * Keyword: Field authorField
			 * To manipulate fields of an object (or static fields of a class), we get a reference
			 * to an object of class `Field`. We can get this reference using the method `getDeclaredField()`
			 * _on the `Class` object_.
			 * 
			 * Note that the object that we want to modify (i.e., the actual book object)
			 * isn't needed.
			 */
			Field authorField = bookClass.getDeclaredField("authors");
			/*?
			 * Keyword: setAccessible
			 * The field `authors` of class `Book` is private, meaning that it can't be accessed or
			 * modified outside the class `Book` (i.e., here). To make it possible to get the
			 * authors, we need to call `setAccessible(true)` on the field object.
			 */
			authorField.setAccessible(true);
			/*?
			 * Keyword: get
			 * The method `get` returns a reference to the value of the field. If the field is a mutable
			 * object, as is the case here (a `List`), we can modify it, and the modifications will be
			 * reflected in the original `book` object.
			 * 
			 * Keyword: book
			 * The argument of `get` is the object of which we want to field value. Here,
			 * we want the authors of the `book` object.
			 * 
			 * If the field is static (i.e., not associated with any object), the argument isn't used,
			 * so we can pass the value `null`.
			 */
			List<String> authors = (List<String>) authorField.get(book);
			authors.add("Brandon Sanderson");

			System.out.println(book);
		}
		/*?
		 * Block: 4
		 * Reflection exceptions
		 * Many methods from the Reflection API throw different exceptions,
		 * most of them are actually subtypes of `ReflectiveOperationException`,
		 * which allows to catch all potential exceptions in the same block.
		 */
		catch (ReflectiveOperationException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Invoke a private instance method of the book object.
	 * 
	 * @param book
	 *            object for which to invoke the private method
	 */
	public static void invokeMethod(Book book)
	{
		try
		{
			/*?
			 * Keyword: bookClass
			 * To get access to methods of an object `X`, the first thing to do is get a reference
			 * to a `Class` object that represents the class of `X`. Here, we get a reference to
			 * the class `Book` using a class literal (`Book.class`).
			 * 
			 * Keyword: Book.class
			 * This is a class literal: an expression that encodes a constant value of type `Class`,
			 * similar to how the _string_ literal `"hello"` encodes a constant value of type String.
			 * 
			 * Here, the class literal represents the class `Book`.
			 */
			Class<?> bookClass = Book.class;
			/*?
			 * Keyword: Method
			 * To invoke methods of an object (or static methods of a class), we get a reference
			 * to an object of class `Method`. We can get this reference using the method `getDeclaredMethod()`
			 * on the `Class` object.
			 * 
			 * Keyword: boolean.class
			 * The first argument of `getDeclaredMethod` is the name of the method, and the following
			 * arguments are the types of its parameters, represented by `Class` objects. Here,
			 * `formatAuthors` has only one parameter, of type `boolean`, so we use the class literal
			 * `boolean.class`.
			 * 
			 * Internal: class literal
			 * boolean.class
			 * Although `boolean` is a primitive type in Java, and therefore not a class, we can still
			 * use a class literal in a similar way, and get a `Class` object that represents this primitive type.
			 */
			Method method = bookClass.getDeclaredMethod("formatAuthors", boolean.class);
			/*?
			 * Keyword: setAccessible
			 * Because the method we want to invoke is private (i.e., not normally accessible here),
			 * it's necessary to call `setAccessible(true)` before invoking the method.
			 */
			method.setAccessible(true);
			/*?
			 * Keyword: invoke(book, true)
			 * To invoke a method represented by a `Method` object, we call `invoke`. The first
			 * argument is the implicit argument of the invoked method, i.e., the book object on
			 * which we would normally call this method. The following arguments are all the explicit
			 * arguments. Here, there is only one explicit argument, which is a boolean.
			 * 
			 * `invoke` returns the value that the invoked method would normally return.
			 * 
			 * Internal: implicit argument
			 * invoke(book, true)
			 * If a method is static, there is no implicit argument, so the first argument of `invoke`
			 * is ignored. We can use the value `null`.
			 */
			String returnValue = (String) method.invoke(book, true);
			System.out.println(returnValue);
		}
		/*?
		 * Block: 4
		 * Reflection exceptions
		 * Many methods from the Reflection API throw different exceptions,
		 * most of them are actually subtypes of `ReflectiveOperationException`,
		 * which allows to catch all potential exceptions in the same block.
		 */
		catch (ReflectiveOperationException e)
		{
			e.printStackTrace();
		}
	}

	public static void invokeConstructor()
	{
		try
		{
			/*?
			 * Keyword: bookClass
			 * To get access to constructors of a class, the first thing to do is get a reference
			 * to a `Class` object that represents that class. Here, we get a reference to
			 * the class `Book` using a class literal (`Book.class`).
			 * 
			 * Keyword: Book.class
			 * This is a class literal: an expression that encodes a constant value of type `Class`,
			 * similar to how the _string_ literal `"hello"` encodes a constant value of type String.
			 * 
			 * Here, the class literal represents the class `Book`.
			 */
			Class<Book> bookClass = Book.class;
			/*?
			 * Keyword: Constructor<Book>
			 * To reflectively create objects of a class, we get a reference to an object of class
			 * `Constructor`. We can get this reference using the method `getDeclaredConstructor()`
			 * on the `Class` object.
			 * 
			 * Keyword: getDeclaredConstructor
			 * The arguments of `getDeclaredConstructor` are the types of its parameters, represented
			 * by `Class` objects. Here, the constructor has two parameters, of type `String` and `List`,
			 * and we use class literals to represent these types.
			 */
			Constructor<Book> constructor = bookClass.getDeclaredConstructor(String.class, List.class);
			/*?
			 * Keyword: newInstance
			 * `newInstance` creates a new object of the target class (here, `Book`), using the
			 * constructor represented the `Constructor` object. The arguments to `newInstance` are
			 * the same arguments we would usually give to the constructor (i.e., the title and authors).
			 */
			Book newBook = constructor.newInstance("The Way of Kings", List.of("Brandon Sanderson"));
			System.out.println(newBook);
		}
		/*?
		 * Block: 4
		 * Reflection exceptions
		 * Many methods from the Reflection API throw different exceptions,
		 * most of them are actually subtypes of `ReflectiveOperationException`,
		 * which allows to catch all potential exceptions in the same block.
		 */
		catch (ReflectiveOperationException e)
		{
			e.printStackTrace();
		}
	}

}

/**
 * A simple class representing a book with a title and a list of authors.
 */
class Book
{

	private String title;
	private List<String> authors;

	public Book(String title, List<String> authors)
	{
		this.title = title;
		this.authors = authors;
	}

	private String formatAuthors(boolean sorted)
	{
		List<String> copy = new ArrayList<>(authors);
		if (sorted)
		{
			Collections.sort(copy);
		}
		switch (copy.size())
		{
		case 0:
			return "unknown authors";
		case 1:
			return copy.get(0);
		case 2:
			return copy.get(0) + " and " + copy.get(1);
		default:
			String list = "";
			for (int i = 0; i < copy.size(); i++)
			{
				if (i > 0)
				{
					list += ", ";
				}
				if (i == copy.size() - 1)
				{
					list += "and ";
				}
				list += copy.get(i);
			}
			return list;
		}
	}

	@Override
	public String toString()
	{
		return title + " by " + formatAuthors(false);
	}
}
