import java.io.*;

public class Util {

    static FileInputStream fileInputStream;
    static BufferedReader bufferedReader;
    static FileWriter fileWriter;

    static void getFile(String path) {
        try {
            Const.sourceFile = new File(path);
            fileInputStream = new FileInputStream(Const.sourceFile);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void makeFile(String path) {
        try {
            Const.targetFile = new File(path);
            fileWriter = new FileWriter(Const.targetFile, true);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void printToken(int num, Const.TokenType tokenType, String tokenString) {
        try {
            switch (tokenType) {
                // IF, ELSE, INT, RETURN, VOID, WHILE,
                case IF:
                case ELSE:
                case INT:
                case RETURN:
                case VOID:
                case WHILE:
                    fileWriter.write("  " + num + ": reserved word : " + tokenString + "\n");
                    break;
                case ASSIGN:
                case EQ:
                case LT:
                case PLUS:
                case MINUS:
                case TIMES:
                case OVER:
                case LPAREN:
                case RPAREN:
                case SEMI:
                case MORE:
                case LESS:
                case COMMA:
                case LSQARE:
                case RSQARE:
                case LLPAREN:
                case RLPAREN:
                    fileWriter.write("  " + num + ": " + tokenString + "\n");
                    break;
                case NUM:
                    fileWriter.write("  " + num + ": NUM, val = " + tokenString + "\n");
                    break;
                case ID:
                    fileWriter.write("  " + num + ": ID, name = " + tokenString + "\n");
                    break;
                case ERROR:
                    fileWriter.write("  " + num + ": ERROR, " + tokenString + "\n");
                    break;
                case ENDFILE:
                    fileWriter.write("  " + num + ": EOF\n");
                    break;
                default:
                    fileWriter.write("  " + num + ": Unknown token\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void printSpace(int n){
        try {
            for(int i=0;i<n;i++){
                fileWriter.write("      ");
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    static void printKind(String str){
        try {
            fileWriter.write(str + "\n");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    static void printTree(TreeNode t){
        while (t != null) {
            printSpace(Parse.step);
            switch (t.nodeKind){
                case DeclaK:
                    switch (t.decla){
                        case VoidK:
                            printKind("VoidK");
                            break;
                        case IntK:
                            printKind("IntK");
                            break;
                        case FuncK:
                            printKind("FuncK");
                            break;
                        case VarDeclK:
                            printKind("Var_DeclK");
                            break;
                    };
                    break;
                case StmtK:
                    switch (t.stmt){
                        case ExpK:
                            printKind("ExpK");
                            break;
                        case CompK:
                            printKind("CompK");
                            break;
                        case RetuK:
                            printKind("Return");
                            break;
                        case SeleK:
                            printKind("If");
                            break;
                        case IteraK:
                            printKind("While");
                            break;
                        case ParamsK:
                            printKind("ParamsK");
                            break;
                        case ParamK:
                            printKind("ParamK");
                            break;
                        case ArgsK:
                            printKind("ArgsK");
                            break;
                        case CallK:
                            printKind("CallK");
                            break;
                    };
                    break;
                case ExpK:
                    switch (t.exp){
                        case IdK:
                            printKind("IdK: " + t.name);
                            break;
                        case OpK:
                            printKind("Op: " + t.name);
                            break;
                        case ConstK:
                            printKind("ConstK: " + t.val);
                            break;
                        case AssignK:
                            printKind("AssignK");
                            break;
                    };
                    break;
            }

            Parse.step++;
            for(TreeNode item : t.child){
                printTree(item);
            }
            Parse.step--;
            t = t.sibling;
        }
    }
}
