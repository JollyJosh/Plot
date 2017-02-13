# Plot
### A Simple Implementation of Scheme

Plot is a simple implementation of Scheme designed to teach the
basics of implementing an interpretive programming language.

## How does it work?

Making an interpreted language consists of a few steps that
include building the following pieces.

1. Lexical Analyzer
2. Parser
3. Environment Module
4. Evaluator

Plot's interpreter will be written in Java 1.8, and will utilize
new features in Java like lambdas.

This is a 5 part workshop. In the first workshop we'll explain
how to run the project, and go over a simple BNF notation for
our language's grammar. And in each of the following workshops
we'll implement each module. The full code will be available before
the workshop.

This git repository will have the full source available upon
the first workshop. Important completed events will be tagged
with their module names, so you can work at your own speed.

During the workshop, we'll program everything until the next
subsequent version and explain it as we go. Important modules
and what commit their found in are in the table below:

|Module            |Commit Hash         |
|------------------|--------------------|
|Grammar           | 9f8c99c            |
|Lexical Analyzer  | e887b04            |
|Parser            | 77f2afc            |
|Environment Module| 234b741            |
|Evaluator         | 7bc1bdb            |

Additional functionality to the language is encouraged,
however the author leaves it up to those experimenting
with languages to implement additional functionality.

## Contributing

If you'd like to contribute, create an issue, fork the repository
and issue a pull request. We'll do our best to review it.
