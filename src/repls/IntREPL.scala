package repls

/*
* todo: first simply do shunting yard, then understand all nuances about class structure & pattern matching,
*  then see where it goes and understand any more if needed...
* */
//test
class IntREPL extends REPLBase {
    type Base = Int
    override val replName: String = "int-repl"

    private var currentBindings: Map[String, Int] = Map.empty

    override def readEval(command: String): String = {
        val tokens : Seq[String] = SplitExpressionString.splitExpressionString(command) //splits all tokens perfectly
        if (tokens.isEmpty) return "empty expression"

        tokens.head match {
            case "@" =>
                val expr = parseExpression(tokens.tail) //remove the @
                simplify(expr)
            case variable if tokens.length >= 3 && tokens(1) == "=" => // variable = tokens.head
                val expr = parseExpression(tokens.drop(2)) //drop(2) takes tokens without the first 2, removing the varname and equals
                assign(variable, expr)
            case _ =>
                val expr = parseExpression(tokens)
                evaluate(expr)
        }
    }

    abstract class Expression { //abstract = no function. We use it so that simplify() later on has a uniform type to work with for all const, var, add, mul, sub
        def eval(bindings : Map[String,Int]) : Int //For separation of concerns we do not use currentBindings...
    }
    case class Const(i : Int) extends Expression {
        override def eval(bindings: Map[String, Int]): Int = i //no storage, just return the value
    }
    case class Var(name: String) extends Expression {
        override def eval(bindings: Map[String, Int]): Int = bindings(name) //returns the value associated with our varname from the provided mapping
    }
    case class Add(left: Expression, right: Expression) extends Expression {
        override def eval(bindings: Map[String, Int]): Int = left.eval(bindings) + right.eval(bindings) //evaluate LHS and RHS (recursion), then add
    }
    case class Mul(left: Expression, right: Expression) extends Expression {
        override def eval(bindings: Map[String, Int]): Int = left.eval(bindings) * right.eval(bindings) //evaluate LHS and RHS (recursion), then mul
    }
    case class Sub(left: Expression, right: Expression) extends Expression {
        override def eval(bindings: Map[String, Int]): Int = left.eval(bindings) - right.eval(bindings) //evaluate LHS and RHS (recursion), then sub
    }

    private def parseExpression(tokens: Seq[String]): Expression = {
        val rpn = shuntingYard(tokens)
        buildExpressionTree(rpn)
    }
    private def precedence(op: String): Int = op match {
        case "+" | "-" => 2
        case "*" => 3
        case _ => 0
    }
    private def shuntingYard(tokens: Seq[String]): Seq[String] = {
        val output = collection.mutable.Queue[String]()
        val stack = collection.mutable.Stack[String]()

        for (token <- tokens) {
            token match {
                case "+" | "-" | "*" =>
                    while (stack.nonEmpty && stack.top != "(" && precedence(stack.top) >= precedence(token)) { //pop until stack top has lower or equal precedence
                        output.enqueue(stack.pop())
                    }
                    stack.push(token)
                case "(" => stack.push(token)
                case ")" =>
                    while (stack.nonEmpty && stack.top != "(") {
                        output.enqueue(stack.pop())
                    }
                    if (stack.nonEmpty && stack.top == "(") {
                        stack.pop()
                    }
                case _ => output.enqueue(token)
            }
        }

        while (stack.nonEmpty) {
            output.enqueue(stack.pop())
        }

        output.toSeq
    }

    private def buildExpressionTree(rpn: Seq[String]): Expression = {

        Const(0)
    }

    private def simplify(expr: Expression): String = {
        // TODO: Implement simplification
        "simplified"
    }

    private def assign(variable: String, expr: Expression): String = {
        // TODO: Implement variable assignment
        s"$variable = assigned"
    }

    private def evaluate(expr: Expression): String = {
        // TODO: Implement evaluation
        "evaluated result"
    }
}

