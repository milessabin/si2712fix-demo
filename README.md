# Demo project exercising the proposed fix for SI-2712

This repository contains a minimal SBT project for experimenting with the fix for [SI-2712][si2712] implemented in
[this fork][si2712fix] of Scala 2.11.8.

The delta against vanilla Scala 2.11.8 is very small (25 lines of code added, half of which are comments) and code
which doesn't fall foul of SI-2712 already will most likely be completely unaffected by the change. In particular the
compiler with this change bootstraps, meaning that it can compile the Scala standard library and itself, without any
other changes. Note that because all the fix does is change the type inference algorithm, compiled binaries should be
compatible in both directions.

I do anticipate that projects which have encountered and worked around SI-2712 will be affected: their workarounds
will sometimes turn into errors ... typically auxilliary implicit definitions will become ambiguous with the primary
definitions, which are now more widely applicable due to more flexible unification. With any luck the fix will be to
just delete those workarounds.

To get a feel for the extent of the impact of this change I would like to get feedback from as many projects as
possible on changes needed (if any) and simplifications made possible (redundant implicits and type annotations which
can be removed). I will also be pestering Lightbend to run a community build with the fix enabled.

It's very late in the day, but I would like to submit this as a pull request for Scala 2.12.0-M5, hidden behind a
language import, -Y option or -Xfuture. Given that the change is very small and localized, binary compatible and
easily made optional, I believe it is very low risk. I believe that the benefits, on the other hand are enormous ...
your reports of successes (especially of gnarly workaround code that can be eliminated) will be hugely important in
making the case for this appearing in a Scala compiler release sooner rather than later.

# Latest news

The implementation I originally published had the limitation that it could only abstract over first order type
parameters and not higher-kinded type parameters. This limitation has been removed in the current build.

The feature is now hidden behind a new Scala compiler flag: add `-Yhigher-order-unification` to enable it.

# Experimenting with this repo

This project contains a minimal `build.sbt` configured to use the modified compiler. It contains a single source file
with the example from [the ticket][si2712]. Things to play with are anything which you would normally expect to have
to use a Cats/Scalaz `Unapply` or `U` suffixed method. Or anything where the arity of the type constructor being
inferred is lower than the arity of the outer type constructor of the concrete type being unified with.

# How to trial this fix with your project

To use this build of the Scala compiler with your own project you should first ensure that it builds correctly with
Scala 2.11.8, then change the Scala version to 2.11.8-tl and add the Maven repository it can be found in to your
build. In the best case scenario this will look something like this,

```scala
lazy val commonSettings = Seq(
  // ...

  resolvers += "scalatl" at "http://milessabin.com/scalatl",
  //scalaVersion := "2.11.8",
  scalaVersion := "2.11.8-tl-201604131941",
  scalaBinaryVersion := "2.11",

  // ...
)
```

see the `build.sbt` in this probject for a minimal example. In more complex cases, for instance in projects which use
full cross builds for some dependencies (eg. for compiler plugins) there will be a little more work to do ... see this
modified [Cats build][catsbuild] for a more elaborate example.

If your project and its upstream dependencies have never had issues with SI-2712 then it is highly likley that if it
builds with vanilla Scala 2.11.8 then it will build with the patched compiler as well.

If your project or some of its upstream dependencies have had issues with SI-2712 and have attempted to work around
them, then it is possible that you will see some errors when compiling with the patched compiler. Typically these will
be the result of additional implicit definitions which were introduced to make up for failures in unification becoming
newly ambiguous with the baseline definitions. In cases like these, the fix is to simply delete the additional
implicit definitions. An example of this can be seen in the [Cats branch][catsbuild] mentioned earlier where four
now-redundant implicit definitions in the tests must be deleted to eliminate ambuguity.

If you run into problems of any other sort, please let me know by creating an issue on this project with full details
of how to reproduce the error.

If you have any notable successes using this modifed compiler and would like to lend your support for lobbying for its
inclusion in Scala 2.12.x then please tweet and blog about it, and please [mail me][mail] with the details.

# Acknowledgements

[Paul Chuisano][paul] originally suggested the fairly simple Haskell-inspired algorithm implemented in the patch in a
[comment][comment] on the original ticket way back in December 2012. It's depressing to think how many hours have been
wasted working round a problem which has such a simple fix.

I'd also like to thank Jason Zaugg, Stefan Zeiger and Seth Tisue for helping me navigate my way around the new SBT
based compiler build ... it's a while since I did any serious Scala compiler hacking and I'm happy to report that the
experience is dramatically improved.

Thanks too to Alistair Johnson and Dale Wijnand for assistance with publishing the compiler binaries.

[si2712]: https://issues.scala-lang.org/browse/SI-2712
[si2712fix]: https://github.com/milessabin/scala/blob/08587aa66c48c453a0aed99cceba26c655cabd65/src/reflect/scala/reflect/internal/Types.scala#L3085-L3124
[catsbuild]: https://github.com/typelevel/cats/compare/v0.4.1...milessabin:topic/si-2712
[mail]: mailto:miles@milessabin.com
[paul]: https://twitter.com/pchiusano
[comment]: https://issues.scala-lang.org/browse/SI-2712?focusedCommentId=61270
