import java.io.File;

public class Const {

    static File sourceFile;
    static File targetFile;
    static final char EOF = (char)-1;

    public enum TokenType {
        // 内务记号
        ENDFILE, ERROR,
        // 保留字
        IF, ELSE, INT, RETURN, VOID, WHILE,
        // 标识符和数字
        ID, NUM,
        // 特殊符号
        ASSIGN, EQ, LT, PLUS, MINUS, TIMES, OVER, LPAREN, RPAREN,
        SEMI, MORE, LESS, COMMA, LSQARE, RSQARE, LLPAREN, RLPAREN
    }

    public enum StateType{
        START, INNUM, INID, INASSIGN, INCOMMENT, DONE
    }

    public enum NodeKind{
        //声明，语句，表达式
        DeclaK, StmtK, ExpK
    }

    public enum DeclaKind{
        VarDeclK, FuncK, IntK, VoidK, ArryDeclK, ArryElemK
    }

    public enum StmtKind{
        CompK, ExpK, SeleK, IteraK, RetuK, CallK, ParamK, ParamsK, ArgsK
    }

    public enum ExpKind{
        OpK, ConstK, IdK, AssignK
    }

    public enum ExpType{
        Void, Integer, Boolean
    }
}
