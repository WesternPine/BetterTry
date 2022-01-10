# BetterTry
A better, more stream-lined try implementation for handling errors in Java.

# JitPack [![](https://jitpack.io/v/WesternPine/BetterTry.svg)](https://jitpack.io/#WesternPine/BetterTry)
Click the icon above to integrate with your code!

# Requirements
 - Java 8+

# Why?
When lambda expressions and functional programming came to java, one thing they forgot about was exceptions. Lambda expressions were meant to produce cleaner and easier to read code. However, when a checked exception is introduced, a one liner piece of code easily turns into 5+, with brackets and miscellaneous code snippits everywhere. And that's not even mentioning how one would go about handling any exceptions produced.

BetterTry passes any possible exceptions down the line of functions, and gracefully handles them to be transformed into a failed function. This way you can continue to write your code, and handle the exception later, or even recover from it to continue writing something that actually makes sense.

# Usage
Usage of BetterTry is meant to be as straight forward and familiar as possible.

Everything you need will come straight from the `Try#of` function. Here you will supply the function with code to execute. Afterwards, whether the code succeeds or fails, you can continue to work with what you have, recover from an error passively, or handle any problems down the line with no disruptions to your code... if you want.

`Try#of` intakes either a TryRunnable, or a TrySupplier. These types MUST be provided as they pass along any possible thrown errors, which is the key to making this all work, however they function exactly the same as a normal runnable or supplier.

An example of what code might look like before implementing BetterTry, where input is a buffered reader of System.in:

```
try {
  System.out.println(input.readLine());
} catch (Exception e) {
  e.printStackTrace();
  System.exit(0);
}
```

This simple task is easily 6 lines of code, and is slightly difficult to follow alongwith and understand. This is made worse if you ever needed to do this in a stream or block of lambda expressions. But don't fret, this is easily compressible AND more understandable. Check out with BetterTry below.

`Try.of(input::readLine).onSuccess(System.out::println).onFailure(Throwable::printStackTrace).onFailure(throwable -> System.exit(0));`

Now thats great and all for dealing with everything in one place, but lets say you needed to get the input to use for something else. Lets recreate the scenario:

```
String in = null;
try {
  in = input.readLine();
} catch (Exception e) {
  System.exit(0);
}
```

You would need to create objects outside the try block, handle exceptions properly, THEN you could deal with the input. This just looks awful when dealing with streams. However, with BetterTry:

`String in = Try.of(input::readLine).onFailure(throwable -> System.exit(0)).getUnchecked();`

I hope the explanation and examples were understandable. If the explanation seems complicated, just follow the logic of the values and their names, and everything should make sense. Additionally, there is more detailed documentation in the library. Happy coding!
