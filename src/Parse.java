import java.util.ArrayList;

public class Parse {

    static Const.TokenType token;
    static int step;

    static void syntaxError(String str) {
        try {
            Util.fileWriter.write("Syntax error at line " + Scan.lineNum + ": " + str + "\n");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void match(Const.TokenType expected) {
        if (token == expected) token = Scan.getToken();
        else {
            syntaxError("unexpected token(" + expected.toString() + ") -> " + token.toString());
        }
    }

    static TreeNode declaration_list() {
        TreeNode t = declaration();
        TreeNode p = t;
        while (token != Const.TokenType.INT && token != Const.TokenType.VOID && token != Const.TokenType.ENDFILE) {
            syntaxError("DeclarationList Error");
            Scan.getToken();
            if (token == Const.TokenType.ENDFILE) break;
        }
        while (token == Const.TokenType.INT || token == Const.TokenType.VOID) {
            TreeNode q = declaration();
            if (q != null) {
                if (t == null) t = p = q;
                else {
                    p.sibling = q;
                    p = q;
                }
            }
        }
        return t;
    }

    static TreeNode declaration() {
        TreeNode t = newDeclaNode(Const.DeclaKind.FuncK);
        if (token == Const.TokenType.INT) {
            t.child.add(newDeclaNode(Const.DeclaKind.IntK));
            match(Const.TokenType.INT);
        } else if (token == Const.TokenType.VOID) {
            t.child.add(newDeclaNode(Const.DeclaKind.VoidK));
            match(Const.TokenType.VOID);
        } else {
            syntaxError("类型匹配错误");
        }

        if (t != null && token == Const.TokenType.ID) {
            TreeNode q = newExpNode(Const.ExpKind.IdK);
            q.name = new String(Scan.tokenString.toString());
            match(Const.TokenType.ID);

            if (token == Const.TokenType.LPAREN) {
                t.child.add(q);
                match(Const.TokenType.LPAREN);
                t.child.add(params());
                match(Const.TokenType.RPAREN);
                t.child.add(compoundStmt());
            } else if (token == Const.TokenType.LSQARE) {
                t.decla = Const.DeclaKind.VarDeclK;
                TreeNode m = newDeclaNode(Const.DeclaKind.ArryDeclK);

                match(Const.TokenType.LSQARE);
                match(Const.TokenType.NUM);
                m.child.add(q);
                m.child.add(newExpNode(Const.ExpKind.ConstK));
                t.child.add(m);
                match(Const.TokenType.RSQARE);
                match(Const.TokenType.SEMI);
            } else if (token == Const.TokenType.SEMI) {
                t.child.add(q);
                match(Const.TokenType.SEMI);
            }
        } else {
            syntaxError("Declaration Error");
        }

        return t;
    }

    static TreeNode params() {
        TreeNode t = newStmtNode(Const.StmtKind.ParamsK);
        TreeNode p = null;

        if (token == Const.TokenType.VOID) {
            p = newDeclaNode(Const.DeclaKind.VoidK);
            match(Const.TokenType.VOID);
            if (token == Const.TokenType.RPAREN) {
                if (t != null) t.child.add(p);
            } else {
                t.child.add(paramList(p));
            }
        } else if (token == Const.TokenType.INT) {
            t.child.add(paramList(p));
        } else {
            syntaxError("Params Error");
        }

        return t;
    }

    static TreeNode paramList(TreeNode k) {
        TreeNode t = param(k);
        TreeNode p = t;
        k = null;

        while (token == Const.TokenType.COMMA) {
            TreeNode q = null;
            match(Const.TokenType.COMMA);
            q = param(k);
            if (q != null) {
                if (t == null) {
                    t = p = q;
                } else {
                    p.sibling = q;
                    p = q;
                }
            }
        }
        return t;
    }

    static TreeNode param(TreeNode k) {
        TreeNode t = newStmtNode(Const.StmtKind.ParamK);
        TreeNode p = null;
        TreeNode q = null;

        if (k == null && token == Const.TokenType.INT) {
            p = newDeclaNode(Const.DeclaKind.IntK);
            match(Const.TokenType.INT);
        } else if (k != null) {
            p = k;
        }
        if (p != null) {
            t.child.add(p);

            if (token == Const.TokenType.ID) {
                q = newExpNode(Const.ExpKind.IdK);
                q.name = new String(Scan.tokenString.toString());
                t.child.add(q);
                match(Const.TokenType.ID);
            } else {
                syntaxError("Param Error");
            }

            if (token == Const.TokenType.LSQARE && t.child.get(1) != null) {
                match(Const.TokenType.LSQARE);
                t.child.add(newExpNode(Const.ExpKind.IdK));
                match( Const.TokenType.RSQARE);
            } else {
                return t;
            }
        }
        return t;
    }

    static TreeNode compoundStmt() {
        TreeNode t = newStmtNode(Const.StmtKind.CompK);
        match(Const.TokenType.LLPAREN);
        t.child.add(localDeclaration());
        t.child.add(statementList());
        match( Const.TokenType.RLPAREN);

        return t;
    }

    static TreeNode localDeclaration() {
        TreeNode t = null;
        TreeNode q = null;
        TreeNode p = null;

        while (token == Const.TokenType.INT || token == Const.TokenType.VOID) {
            p = newDeclaNode(Const.DeclaKind.VarDeclK);
            if (token == Const.TokenType.INT) {
                p.child.add(newDeclaNode(Const.DeclaKind.IntK));
                match(Const.TokenType.INT);
            } else if (token == Const.TokenType.VOID) {
                p.child.add(newDeclaNode(Const.DeclaKind.VoidK));
                match(Const.TokenType.VOID);
            }
            if (p != null && token == Const.TokenType.ID) {
                TreeNode tmp = newExpNode(Const.ExpKind.IdK);
                tmp.name = new String(Scan.tokenString.toString());
                p.child.add(tmp);
                match(Const.TokenType.ID);

                if (token == Const.TokenType.LSQARE) {
                    p.child.add(newDeclaNode(Const.DeclaKind.VarDeclK));
                    match(Const.TokenType.LSQARE);
                    match(Const.TokenType.RSQARE);
                    match(Const.TokenType.SEMI);
                } else {
                    match(Const.TokenType.SEMI);
                }
            } else {
                syntaxError("LocalDeclaration Error");
            }

            if (p != null) {
                if (t == null)t = q = p;
                else {
                    q.sibling = p;
                    q = p;
                }
            }
        }
        return t;
    }

    static TreeNode statementList() {
        TreeNode t = statement();
        TreeNode p = t;

        while (token == Const.TokenType.IF || token == Const.TokenType.LLPAREN ||
                token == Const.TokenType.ID || token == Const.TokenType.WHILE ||
                token == Const.TokenType.RETURN || token == Const.TokenType.SEMI ||
                token == Const.TokenType.LPAREN || token == Const.TokenType.NUM) {
            TreeNode q = statement();
            if (q != null) {
                if (t == null) {
                    t = p = q;
                } else {
                    p.sibling = q;
                    p = q;
                }
            }
        }
        return t;
    }

    static TreeNode statement() {
        TreeNode t = null;
        switch (token) {
            case IF:
                t = selectionStmt();
                break;
            case WHILE:
                t = iterationStmt();
                break;
            case RETURN:
                t = returnStmt();
                break;
            case LLPAREN:
                t = compoundStmt();
                break;
            case ID:
            case SEMI:
            case LPAREN:
            case NUM:
                t = expressionStmt();
                break;
            default:
                syntaxError("Statement Error");
                token = Scan.getToken();
                break;
        }
        return t;
    }

    static TreeNode selectionStmt() {
        TreeNode t = newStmtNode(Const.StmtKind.SeleK);
        match(Const.TokenType.IF);
        match(Const.TokenType.LPAREN);

        if (t != null) {
            t.child.add(expression());
        }
        match(Const.TokenType.RPAREN);
        t.child.add(statement());

        if (token == Const.TokenType.ELSE) {
            match(Const.TokenType.ELSE);
            if (t != null) {
                t.child.add(statement());
            }
        }
        return t;
    }

    static TreeNode expressionStmt() {
        TreeNode t = null;
        if (token == Const.TokenType.SEMI) {
            match(Const.TokenType.SEMI);
            return t;
        } else {
            t = expression();
            match(Const.TokenType.SEMI);
        }
        return t;
    }

    static TreeNode iterationStmt() {
        TreeNode t = newStmtNode(Const.StmtKind.IteraK);
        match(Const.TokenType.WHILE);
        match(Const.TokenType.LPAREN);
        if (t != null) {
            t.child.add(expression());
        }
        match(Const.TokenType.RPAREN);
        if (t != null) {
            t.child.add(statement());
        }
        return t;
    }

    static TreeNode returnStmt() {
        TreeNode t = newStmtNode(Const.StmtKind.RetuK);
        match(Const.TokenType.RETURN);

        if (token == Const.TokenType.SEMI) {
            match(Const.TokenType.SEMI);
            return t;
        } else {
            if (t != null) {
                t.child.add(expression());
            }
        }
        match(Const.TokenType.SEMI);

        return t;
    }

    static TreeNode expression() {
        TreeNode t = var();
        if (t == null) {
            t = simpleExpression(t);
        } else {
            TreeNode p = null;
            if (token == Const.TokenType.EQ) {
                p = newExpNode(Const.ExpKind.AssignK);
                p.name = new String(Scan.tokenString.toString());
                match(Const.TokenType.EQ);
                p.child.add(t);
                p.child.add(expression());
                return p;
            } else {
                t = simpleExpression(t);
            }
        }
        return t;
    }

    static TreeNode simpleExpression(TreeNode k) {
        TreeNode t = additiveExpression(k);
        k = null;

        if (token == Const.TokenType.ASSIGN || token == Const.TokenType.MORE || token == Const.TokenType.LESS) {
            TreeNode q = newExpNode(Const.ExpKind.OpK);
            q.op = token;
            q.name = new String(Scan.tokenString.toString());
            q.child.add(t);
            t = q;
            match(token);
            t.child.add(additiveExpression(k));
            return t;
        }
        return t;
    }

    static TreeNode additiveExpression(TreeNode k) {
        TreeNode t = term(k);
        k = null;

        while (token == Const.TokenType.PLUS || token == Const.TokenType.MINUS) {
            TreeNode q = newExpNode(Const.ExpKind.OpK);
            q.op = token;
            q.name = new String(Scan.tokenString.toString());
            q.child.add(t);
            match(token);
            q.child.add(term(k));
            t = q;
        }

        return t;
    }

    static TreeNode term(TreeNode k) {
        TreeNode t = factor(k);
        k = null;

        while (token == Const.TokenType.TIMES || token == Const.TokenType.OVER) {
            TreeNode q = newExpNode(Const.ExpKind.OpK);
            q.op = token;
            q.name = new String(Scan.tokenString.toString());
            q.child.add(t);
            match(token);
            q.child.add(factor(k));
            t = q;
        }
        return t;
    }

    static TreeNode factor(TreeNode k) {
        TreeNode t = null;

        if (k != null) {
            if (token == Const.TokenType.LPAREN) {
                t = call(k);
            } else {
                t = k;
            }
        } else {
            switch (token) {
                case LPAREN:
                    match(Const.TokenType.LPAREN);
                    t = expression();
                    match(Const.TokenType.RPAREN);
                    break;
                case ID:
                    k = var();
                    if (token == Const.TokenType.LPAREN) {
                        t = call(k);
                    }else {
                        t = k;
                    }
                    break;
                case NUM:
                    t = newExpNode(Const.ExpKind.ConstK);
                    if (t != null && token == Const.TokenType.NUM) {
                        t.val = Integer.parseInt(Scan.tokenString.toString());
                    }
                    match(Const.TokenType.NUM);
                    break;
                default:
                    syntaxError("Factor Error: " + token.toString());
                    token = Scan.getToken();
                    break;
            }
        }
        return t;
    }

    static TreeNode var(){
        TreeNode t = null;
        TreeNode p = null;
        TreeNode q = null;
        if (token == Const.TokenType.ID) {
            p = newExpNode(Const.ExpKind.IdK);
            p.name = new String(Scan.tokenString.toString());
            match(Const.TokenType.ID);
            if (token == Const.TokenType.LSQARE) {
                match(Const.TokenType.LSQARE);
                q = expression();
                match(Const.TokenType.RSQARE);

                t = newDeclaNode(Const.DeclaKind.ArryElemK);
                t.child.add(p);
                t.child.add(q);
            }else {
                t = p;
            }
        }
        return t;
    }

    static TreeNode call(TreeNode k){
        TreeNode t = newStmtNode(Const.StmtKind.CallK);
        if (k != null)t.child.add(k);
        match(Const.TokenType.LPAREN);

        if (token == Const.TokenType.RPAREN) {
            match(Const.TokenType.RPAREN);
            return t;
        } else if (k != null) {
            t.child.add(args());
            match(Const.TokenType.RPAREN);
        }
        return t;
    }

    static TreeNode args(){
        TreeNode t = newStmtNode(Const.StmtKind.ArgsK);
        TreeNode s = null;
        TreeNode p = null;

        if (token != Const.TokenType.RPAREN) {
            s = expression();
            p = s;
            while (token == Const.TokenType.COMMA) {
                TreeNode q = null;
                match(Const.TokenType.COMMA);
                q = expression();
                if (q != null) {
                    if (s == null) {
                        s = p = q;
                    }else {
                        p.sibling = q;
                        p = q;
                    }
                }
            }
        }
        if (s != null) {
            t.child.add(s);
        }
        return t;
    }

    static TreeNode newDeclaNode(Const.DeclaKind kind) {
        TreeNode t = new TreeNode();
        if (t == null) {
            try {
                Util.fileWriter.write("Out of memory error at line " + Scan.lineNum + " \n");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        t.nodeKind = Const.NodeKind.DeclaK;
        t.child = new ArrayList<>();
        t.decla = kind;
        t.lineNum = Scan.lineNum;

        return t;
    }

    static TreeNode newExpNode(Const.ExpKind kind) {
        TreeNode t = new TreeNode();
        if (t == null) {
            try {
                Util.fileWriter.write("Out of memory error at line " + Scan.lineNum + " \n");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        t.nodeKind = Const.NodeKind.ExpK;
        t.child = new ArrayList<>();
        t.exp = kind;
        t.lineNum = Scan.lineNum;

        return t;
    }

    static TreeNode newStmtNode(Const.StmtKind kind) {
        TreeNode t = new TreeNode();
        if (t == null) {
            try {
                Util.fileWriter.write("Out of memory error at line " + Scan.lineNum + " \n");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        t.nodeKind = Const.NodeKind.StmtK;
        t.child = new ArrayList<>();
        t.stmt = kind;
        t.lineNum = Scan.lineNum;

        return t;
    }

    static TreeNode parse() {
        step = 0;
        TreeNode root = new TreeNode();
        token = Scan.getToken();
        root = declaration_list();
        if (token != Const.TokenType.ENDFILE)
            syntaxError("Code ends begore file\n");
        return root;
    }
}
