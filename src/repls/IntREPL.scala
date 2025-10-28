package repls

/*
* todo: first simply do shunting yard, then understand all nuances about class structure & pattern matching,
*  then see where it goes and undersgtand any more if needed...
* */

class IntREPL extends REPLBase {
    type Base = Int
    override val replName: String = "int-repl"

    override def readEval(command: String): String = {
        val tokens : Seq[String] = SplitExpressionString.splitExpressionString(command)
        if (tokens.isEmpty) return "empty expression"

        tokens.head match {
            case "@" =>
                val expr = parseExpression(tokens.tail)
                simplify(expr)
            case variable if tokens.length >= 3 && tokens(1) == "=" => // variable = tokens.head
                val expr = parseExpression(tokens.drop(2)) //drop(2) takes tokens without the first 2
                assign(variable, expr)
            case _ =>
                val expr = parseExpression(tokens)
                evaluate(expr)
        }
    }

    abstract class Expression {
        def eval(bindings : Map[String,Int]) : Int
    }
    case class Const(i : Int) extends  Expression {
        override def eval(bindings: Map[String, Int]): Int = i
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

    private def parseExpression(tokens: Seq[String]): Expression = {
        val rpn = shuntingYard(tokens)
        buildExpressionTree(rpn)
    }
    private def shuntingYard(tokens: Seq[String]): Seq[String] = {
        Seq.empty
    }

    private def buildExpressionTree(rpn: Seq[String]): Expression = {
        Const(0)
    }
}
