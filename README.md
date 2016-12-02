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

## License

Copyright (c) 2016 UA ACM

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software
without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to
whom the Software is furnished to do so, subject to the
following conditions:

The above copyright notice and this permission notice shall
be included in all copies or substantial portions of the
Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.