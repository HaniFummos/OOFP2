package repls

import scala.collection.mutable

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
                val simplified = simplify(expr, currentBindings)  // Returns Expression
                prettyPrint(simplified)
            case variable if tokens.length >= 3 && tokens(1) == "=" => // variable = tokens.head
                val expr = parseExpression(tokens.drop(2)) //drop(2) takes tokens without the first 2, removing the varname and equals
                assign(variable, expr)
            case _ =>
                val expr = parseExpression(tokens)
                evaluate(expr)
        }
    }

    abstract class Expression { //abstract = no function. We use it so that simplify() later on has a uniform type to work with for all const, var, add, mul, sub
        def value(bindings : Map[String,Int]) : Int //For separation of concerns & modularity we do not use currentBindings :)
    }
    protected case class Const(i : Int) extends Expression {
        override def value(bindings: Map[String, Int]): Int = i //no storage, just return the value
    }
    protected case class Var(name: String) extends Expression {
        override def value(bindings: Map[String, Int]): Int = bindings(name) //returns the value associated with our varname from the provided mapping
    }
    protected case class Add(left: Expression, right: Expression) extends Expression {
        override def value(bindings: Map[String, Int]): Int = left.value(bindings) + right.value(bindings) //evaluate LHS and RHS (recursion), then add
    }
    protected case class Mul(left: Expression, right: Expression) extends Expression {
        override def value(bindings: Map[String, Int]): Int = left.value(bindings) * right.value(bindings) //evaluate LHS and RHS (recursion), then mul
    }
    protected case class Sub(left: Expression, right: Expression) extends Expression {
        override def value(bindings: Map[String, Int]): Int = left.value(bindings) - right.value(bindings) //evaluate LHS and RHS (recursion), then sub
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
        val output = mutable.Queue[String]()
        val stack = mutable.Stack[String]()

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

    def isNumber(s: String): Boolean = {
        if (s.isEmpty) return false

        val charsToCheck = if (s.head == '-') {
            if (s.length == 1) return false
            s.tail
        }
        else {
            s
        }

        charsToCheck.forall(_.isDigit)
    }

    private def buildExpressionTree(rpn: Seq[String]): Expression = {
        val stack = mutable.Stack[Expression]()

        for (token <- rpn) {
            token match {
                case "+" =>
                    val right = stack.pop()
                    val left = stack.pop()
                    stack.push(Add(left, right))

                case "-" =>
                    val right = stack.pop()
                    val left = stack.pop()
                    stack.push(Sub(left, right))

                case "*" =>
                    val right = stack.pop()
                    val left = stack.pop()
                    stack.push(Mul(left, right))

                case t if isNumber(t) =>
                    stack.push(Const(t.toInt))

                case _ => //variables
                    stack.push(Var(token))
            }
        }

        if (stack.isEmpty) Const(0) else stack.pop()
    }

    private def simplify(expr: Expression, bindings: Map[String, Int]): Expression = { //bottom-up
        expr match {
            case Const(i) => Const(i)
            case Var(name) =>
                bindings.get(name) match {
                    case Some(value) => Const(value)
                    case None => Var(name)
                }

            case Add(left, right) =>
                val sl = simplify(left, bindings)
                val sr = simplify(right, bindings)
                simplifyAdd(sl, sr, bindings)

            case Mul(left, right) =>
                val sl = simplify(left, bindings)
                val sr = simplify(right, bindings)
                simplifyMul(sl, sr)

            case Sub(left, right) =>
                val sl = simplify(left, bindings)
                val sr = simplify(right, bindings)
                simplifySub(sl, sr)
        }
    }

    private def simplifyAdd(left: Expression, right: Expression, bindings: Map[String, Int]): Expression = {
        (left, right) match {
            case (Const(0), r) => r
            case (l, Const(0)) => l
            case (Const(a), Const(b)) => Const(a + b) //easy cases...

            case (Mul(a1, b), Mul(a2, c)) if a1 == a2 => Mul(simplify(a1, bindings), simplify(Add(b, c), bindings)) //(even though we do bottom up, we will guaranteed catch these patterns eventually)
            case (Mul(b, a1), Mul(a2, c)) if a1 == a2 => Mul(simplify(a1, bindings), simplify(Add(b, c), bindings))
            case (Mul(a1, b), Mul(c, a2)) if a1 == a2 => Mul(simplify(a1, bindings), simplify(Add(b, c), bindings))
            case (Mul(b, a1), Mul(c, a2)) if a1 == a2 => Mul(simplify(a1, bindings), simplify(Add(b, c), bindings))

            case _ => Add(left, right) // Default, for example add(x,y) with both unbound and no helpful distributivity anywhere
        }
    }

    private def simplifyMul(left: Expression, right: Expression): Expression = {
        (left, right) match {
            case (Const(0), _) => Const(0)
            case (_, Const(0)) => Const(0)
            case (Const(1), r) => r
            case (l, Const(1)) => l
            case (Const(a), Const(b)) => Const(a * b)
            case _ => Mul(left, right)
        }
    }

    private def simplifySub(left: Expression, right: Expression): Expression = {
        (left, right) match {
            case (l, r) if l == r => Const(0)
            case (Const(a), Const(b)) => Const(a - b)
            case _ => Sub(left, right)
        }
    }

    private def prettyPrint(expr: Expression): String = expr match {
        case Const(i) => i.toString
        case Var(name) => name
        case Add(left, right) => s"${parenthesesCheck(left, "+")} + ${parenthesesCheck(right, "+")}"
        case Mul(left, right) => s"${parenthesesCheck(left, "*")} * ${parenthesesCheck(right, "*")}"
        case Sub(left, right) => s"${parenthesesCheck(left, "-")} - ${parenthesesCheck(right, "-")}"
    }

    private def parenthesesCheck(expr: Expression, parentOperator: String): String = {
        val needsParentheses = expr match { // (l1 op2 l2) op (r1 op2 r2) --> if op2 < op, need parentheses
            case Add(_, _) if precedence("+") < precedence(parentOperator) => true //I know that checking if parentOperator is * has the same effect...
            case Sub(_, _) if precedence("-") < precedence(parentOperator) => true
            case _ => false
        }

        val str = prettyPrint(expr)
        if (needsParentheses) s"( $str )" else str
    }

    private def assign(variable: String, expr: Expression): String = {
        try {
            val value = expr.value(currentBindings) //get the single constant value of our expression. Throws an error if there's an unbound variable
            currentBindings = currentBindings + (variable -> value)
            s"$variable = $value"
        }
        catch {
            case e: NoSuchElementException =>
                val missingVar = e.getMessage.replace("key not found: ", "")
                s"Unknown variable: $missingVar"
        }
    }

    private def evaluate(expr: Expression): String = {
        try {
            val value = expr.value(currentBindings)
            value.toString
        }
        catch {
            case e: NoSuchElementException =>
                val missingVar = e.getMessage.replace("key not found: ", "")
                s"Unknown variable: $missingVar"
        }
    }
}