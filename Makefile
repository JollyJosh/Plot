main : all

environment : compile
	cd bin && jar cmf ../manifests/environment.manifest Environment.jar *

run :
	java -jar bin/Environment.jar

clean :
	rm -rf bin

bin :
	mkdir -p bin

compile : clean bin
	javac -cp bin -d bin src/edu/ua/cs/acm/Plot/*.java

lexer : compile
	cd bin && jar cmf ../manifests/lexer.manifest Lexer.jar *

recognizer : compile
	cd bin && jar cmf ../manifests/recognizer.manifest Recognizer.jar *

evaluator : compile
	cd bin && jar cmf ../manifests/evaluator.manifest Evaluator.jar *

plot : evaluator
