package icse.demo.functionalprogramming;

import java.util.List;
import java.util.function.Consumer;

/*
 * Class demonstrating the use of lambda expressions in Java. Usage examples are adapted from
 * https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html
 */
public class FunctionalProgrammingDemo {
	
	/**
	 * This main method demonstrates 6 equivalent ways to loop through the `people` list and print only people who are
	 * 30 or over, using anonymous classes, lambda expressions, and method references.
	 * 
	 * @param args
	 *            command line arguments (not used)
	 */
	public static void main(String[] args) {
		List<Person> people = List.of(new Person("John", "Doe", 18),
				new Person("Jane", "Doe", 25),
				new Person("Fred", "Smith", 41),
				new Person("George", "McIntosh", 30),
				new Person("Bob", "Bob", 21));
		
		/*?
		 * Block: 7
		 * Approach 1: Anonymous class
		 * This approach instantiates an interface using an anonymous class.
		 * Anonymous classes are available since Java 1.1.
		 * 
		 * Internal: anonymous class
		 * 7
		 * An anonymous class can create more concise code by declaring and
		 * instantiating a class at the same time. In this case, it creates
		 * a new class that implements the `CheckPerson` interface. It implements
		 * the only method `check(Person)` checking if the person's age is 30
		 * or more.
		 * 
		 * The syntax to create an anonymous class is similar to invoking a
		 * constructor, except that it is followed by a block of code that
		 * contains the class definition.
		 * 
		 * Internal: block of code
		 * anonymous class
		 * code within `{` and `}`, here containing the implementation
		 * of the `check(Person)` method.
		 */
		CheckPerson anonymousClass = new CheckPerson() {
			
			@Override
			public boolean check(Person person) {
				return person.age >= 30;
			}
		};
		printPeople(people, anonymousClass);
		
		/*?
		 * Block: 3
		 * Approach 2: Lambda expression (Block)
		 * This approach instantiates the `CheckPerson` interface using a
		 * lambda expression (from "lambda calculus") with a block.
		 * 
		 * Lambda expression can only be used to create instances of
		 * functional interfaces.
		 * 
		 * The syntax of a lambda expression starts with the parameter name
		 * (here, `person`), followed by the arrow operator (`->`), and then
		 * a block of code that contains the definition of the only
		 * method from the functional interface.
		 * 
		 * Internal: lambda calculus
		 * 3
		 * Lambda calculus is a formal system in mathematics to express computations.
		 * 
		 * A lambda expression is an expression that defines a function without
		 * _naming_ this function (similar to anonymous class) and without
		 * declaring the types of its parameters. The parameter types are inferred
		 * by the compiler. 
		 * 
		 * Internal: functional interfaces
		 * 3
		 * Any interface that declares a single method is a functional interface.
		 * 
		 * Internal: a single method
		 * functional interfaces
		 * `static` and `default` methods do not count for this definition (i.e.,
		 * a functional interface has exactly one `abstract` method)
		 * 
		 * Internal: the parameter name
		 * 3
		 * If there is no parameter to the functional interface's method, use an
		 * empty set of parentheses. E.g., `() -> {  body of the method  }`.
		 * 
		 * If there are more than one parameter, enclose them in parentheses, and
		 * separate them with commas. E.g., `(x, y, z) -> { body of the method }`.
		 * 
		 * Internal: method from the functional interface
		 * 3
		 * Here, you can see that the block following the arrow `->` is exactly
		 * the same as the body of `check(Person)` in the previous anonymous class.
		 */
		CheckPerson lambdaBlock = person -> {
			return person.age >= 30;
		};
		printPeople(people, lambdaBlock);
		
		/*?
		 * Block: 1
		 * Approach 3: Lambda expression (No block)
		 * When the code block in a lambda expression consists only of
		 * returning a value (as in this case), the code block can be replaced with
		 * only the returned value.
		 * 
		 * Here, for example, the code block
		 * ```java
		 * { return person.age >= 30; }
		 * ```
		 * on the right side of the arrow `->` can be replaced with just
		 * ```java
		 * person.age >= 30
		 * ```
		 * 
		 * Keyword: person -> person.age >= 30;
		 * Equivalent to the anonymous class
		 * ```java
		 * new CheckPerson() {
		 *     @Override
		 *     public boolean check(Person person) {
		 *         return person.age >= 30;
		 *     }
		 * }
		 * ```
		 */
		CheckPerson lambdaExpression = person -> person.age >= 30;
		printPeople(people, lambdaExpression);
		
		/*?
		 * Block: 1
		 * Approach 4: Static method reference
		 * Instead of using lambda expressions, if there is already a method that
		 * does what is needed (here, check if a person is 30 or older), it's possible
		 * to use a _reference to this method_ to instantiate a functional interface.
		 * 
		 * The method being used (here, `isOver30(Person)`) must have the same parameter
		 * types and the same return type as the method of the functional interface.
		 * 
		 * The syntax of a method reference starts with the class that declares the
		 * method (here, `FunctionalProgramming`), followed by two colons (`::`),
		 * followed by the name of the method without its parameters (here, `isOver30`).
		 * 
		 * Internal: same return type
		 * 1
		 * If the method of the functional interface returns `void`, then the method
		 * reference can return any type, but the returned value isn't used.
		 * 
		 * Keyword: isOver30
		 * Equivalent to the anonymous class
		 * ```java
		 * new CheckPerson() {
		 *     @Override
		 *     public boolean check(Person person) {
		 *         return FunctionalProgramming.isOver30(person);
		 *     }
		 * }
		 * ```
		 */
		CheckPerson methodReference = FunctionalProgrammingDemo::isOver30;
		printPeople(people, methodReference);
		
		/*?
		 * Block: 1
		 * Approach 5: Instance method reference
		 * Method references can also be used for instance methods. In this case,
		 * the first argument of the functional interface's method must be the
		 * implicit argument of the instance method. Here, the first (and only)
		 * argument is of type `Person`, so it's possible to use an instance method
		 * from the class `Person` (with no other, or explicit, argument).
		 * 
		 * The syntax is the same as for method references for static method: the
		 * name of the class, followed by `::`, followed by the name of the method.
		 * 
		 * Internal: implicit argument
		 * 1
		 * The object _on which the method is invoked_ (i.e., what comes before the
		 * dot `.`) is the implicit argument of this method. For example, in the
		 * expression `somePerson.isOld()`, the variable `somePerson` is the implicit
		 * argument of that method call.
		 * 
		 * Static methods have no implicit argument.
		 * 
		 * Internal: explicit
		 * 1
		 * Explicit arguments are those provided within the parentheses. For example,
		 * in the expression `isOver30(somePerson)`, the variable `somePerson` is the
		 * explicit argument of that method call.
		 * 
		 * Keyword: isOld
		 * Equivalent to the anonymous class
		 * ```java
		 * new CheckPerson() {
		 *     @Override
		 *     public boolean check(Person person) {
		 *         return person.isOld();
		 *     }
		 * }
		 * ```
		 */
		CheckPerson methodReference2 = Person::isOld;
		printPeople(people, methodReference2);
		
		/*?
		 * Block: 1
		 * Approach 6: In-line functional programming
		 * Functional programming can create very compact code, by in-lining
		 * lambda expressions and method references.
		 * 
		 * Here, the 2nd argument to `processPeople` is a `CheckPerson`, which
		 * is instantiated with the lambda expression `p -> p.age >= 30` (similar
		 * to approach 3 above). The 3rd argument is a `Consumer`, which is
		 * instantiated with the method reference `FunctionalProgramming::print`
		 * (similar to approach 4 above).
		 * 
		 * Internal: Consumer
		 * 1
		 * `Consumer` is a generic functional interface for functions that take a
		 * single argument and return `void`.
		 * 
		 * Internal: generic
		 * Consumer
		 * It is possible to specify the type of the argument for the method of
		 * the `Consumer` interface, by using angle brackets. See the definition
		 * of `processPeople` for example: `Consumer<Person>` means that the
		 * argument must be of type `Person`.
		 */
		processPeople(people, p -> p.age >= 30, FunctionalProgrammingDemo::print);
	}
	
	public static boolean isOver30(Person person) {
		return person.age >= 30;
	}
	
	public static void print(Person person) {
		System.out.println(person);
	}
	
	/**
	 * Loops through the list of people and print those that meet the criteria defined by the CheckPerson
	 * implementation.
	 * 
	 * @param people
	 *            list of people to check
	 * @param filter
	 *            captures the condition to check
	 */
	public static void printPeople(List<Person> people, CheckPerson filter) {
		for (Person person : people) {
			if (filter.check(person)) {
				System.out.println(person);
			}
		}
	}
	
	/**
	 * Similar to printPeople, but uses a more general `Consumer` to capture what action to perform on the people that
	 * pass the filter.
	 * 
	 * @param people
	 *            list of people to check
	 * @param filter
	 *            captures the condition to check
	 * @param action
	 *            captures the action to perform
	 */
	public static void processPeople(List<Person> people, CheckPerson filter, Consumer<Person> action) {
		for (Person person : people) {
			if (filter.check(person)) {
				action.accept(person);
			}
		}
	}
	
	/**
	 * A simple class representing a person, with a first name, last name, and age.
	 */
	public static class Person {
		
		private final String firstName;
		private final String lastName;
		private final int age;
		
		public Person(String firstName, String lastName, int age) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.age = age;
		}
		
		public String getFirstName() {
			return firstName;
		}
		
		public String getLastName() {
			return lastName;
		}
		
		public int getAge() {
			return age;
		}
		
		public boolean isOld() {
			return age >= 30;
		}
		
		@Override
		public String toString() {
			return firstName + " " + lastName;
		}
	}
	
	/**
	 * Interface for a person filter: indicates if a person respects some condition defined by the implementation.
	 */
	/*?
	 * Keyword: @FunctionalInterface
	 * A _functional interface_ in Java is an interface that declares a
	 * single abstract method. Lambda expressions and method reference
	 * expressions can only be used to instantiate functional interfaces.
	 * 
	 * The annotation `@FunctionalInterface` can be used to mark functional
	 * interfaces, but _they are not required_. Any interface with a single
	 * method is a functional interface.
	 * 
	 * Internal: single abstract method
	 * @FunctionalInterface
	 * The interface can declare other (non-abstract) `static` and/or `default`
	 * methods. It does not prevent it from being a functional interface.
	 */
	@FunctionalInterface
	public static interface CheckPerson {
		
		public boolean check(Person person);
	}
}
