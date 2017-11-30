package amarnehsoft.com.debits.controllers;

import android.graphics.Path;
import android.util.Log;

import amarnehsoft.com.debits.utils.NumberUtils;

/**
 *
 * @author Amarneh
 */
public class CalculatorController {

    public static final String DOT=".";
    private String mQuery;

    public enum Operator{
        MULTIPLY("*",2),DIVID("/",2),PLUS("+",1),MINUS("-",1);
        private String operator;
        private int priority; // high priority executed first
        private Operator(String operator,int priority){
            this.priority = priority;
            this.operator=operator;
        }

        public int getPriority(){
            return priority;
        }

        @Override
        public String toString() {
            return operator;
        }
    }

    private static CalculatorController instance;
    public static CalculatorController getInstance(){
        if (instance == null)
            instance = new CalculatorController();
        return instance;
    }
    private CalculatorController(){
        mQuery="";
    }

    public double calculate(){
        return getValue(mQuery);
    }

    public double getValue(String query){
        Log.e("Amarneh","query=" + query);
        String formatedQuery = format(query);
        Log.e("Amarneh","formatedQuery="+formatedQuery);
        int numberOfOp = numberOfOperators(formatedQuery);
        Log.e("Amarneh","numberOfOp="+numberOfOp);
        int index=0;
        
        while (numberOfOp != 0){
            String[] numbers = new String[3];
            Operator[] operators = new Operator[2];

            int numberIndex=0;
            int operatorIndex=0;
            for (int i=0;i<formatedQuery.length();i++){
                index++;
                String letter = formatedQuery.substring(i,i+1);
                Operator op = getOperator(letter);
                boolean isOperand = op != null && (i!=0);
                if (!isOperand){
                    if (numbers[numberIndex] == null) numbers[numberIndex] = "";
                    numbers[numberIndex] = numbers[numberIndex]+letter;
                } else{
                    if (operatorIndex == 2){
                        index--;
                        break;
                    }
                    else {
                        numberIndex++;
                        operators[operatorIndex] = op;
                        operatorIndex++;
                    }
                }
            }

            Log.e("Amarneh","index="+index);
            
            for (int i=0;i<numbers.length;i++){
                if (numbers[i] == null || numbers[i].isEmpty()) numbers[i] = "0";
                if (numbers[i].startsWith(".")) numbers[i] = "0" + numbers[i];
            }
            for (int i=0;i<operators.length;i++){
                if (operators[i] == null) operators[i] = Operator.PLUS;
            }

            double number1 = NumberUtils.getDouble(numbers[0]);
            double number2 = NumberUtils.getDouble(numbers[1]);
            double number3 = NumberUtils.getDouble(numbers[2]);

            Log.e("Amarneh","number1="+number1);
            Log.e("Amarneh","number2="+number2);
            Log.e("Amarneh","number3="+number3);
           
            double result;
            String resultStr="";
            int p1= operators[0].getPriority();
            int p2= operators[1].getPriority();
            Log.e("Amarneh","p1="+p1);
            Log.e("Amarneh","p2="+p2);
            
            if (p1 >= p2){
                result = calculate(number1,operators[0],number2);
                if((operators[1] == Operator.PLUS || operators[1] == Operator.MINUS) && number3==0)
                   resultStr = result+"";
                else
                    resultStr = result + operators[1].toString()+number3;
            }else {
                result = calculate(number2,operators[1],number3);
                if((operators[0] == Operator.PLUS || operators[1] == Operator.MINUS) && number1==0)
                    resultStr =result+"";
                else
                    resultStr = number1 + operators[0].toString() + result;
            }
            Log.e("Amarneh","result="+result);
            Log.e("Amarneh","resultStr="+resultStr);
            int len = index;
            Log.e("Amarneh","len="+len);
            index=0;
            if (len > 0){
                formatedQuery = resultStr + formatedQuery.substring(len);
                numberOfOp = numberOfOperators(formatedQuery);
            }


            Log.e("Amarneh","formted query="+formatedQuery);
            Log.e("Amarneh","numberOfOp="+numberOfOp);
        }

        double res=0;
        try{
            res= Double.parseDouble(formatedQuery);
            if (res == Double.POSITIVE_INFINITY || res == Double.NEGATIVE_INFINITY) res = 0;
        }catch(Exception e){
            Log.e("Amarneh","exception >>>>>>>>>> ");
        }
       
        return res;
    }

    private double calculate(double number1,Operator operator,double number2){
        if (operator == Operator.PLUS) return number1 + number2;
        if (operator == Operator.MINUS) return number1 - number2;
        if (operator == Operator.MULTIPLY) return number1*number2;
        if (operator == Operator.DIVID){
            if (number2 == 0) return 0;
            return number1/number2;
        }
        return 0;
    }

    private Operator getOperator(String operator){
        for (Operator op : Operator.values()){
            if (op.toString().equals(operator)) return op;
        }
        return null;
    }

    private int numberOfOperators(String query){
        int count=0;
        //if (query.startsWith(Operator.MINUS.toString())) query = "0"+query;
        for (int i=0;i<query.length();i++){
            if (getOperator(query.substring(i,i+1)) != null && i!=0) count++;
        }
        return count;
    }

    private String format(String str){
        String newString = str.replace(" ","");
        //if (newString.startsWith(Operator.MINUS.toString())) newString= "0"+newString;
        newString.replace("x",Operator.MULTIPLY.toString());
        newString = newString.replace("X",Operator.MULTIPLY.toString());

        newString = newString.replace("\\", Operator.DIVID.toString());
        newString = newString.replace("_", Operator.MINUS.toString());
        newString = newString.replace("-", Operator.MINUS.toString());
        newString = newString.replace(",",DOT);
        newString = newString.replace("'",DOT);
        return newString;
    }

    public boolean append(String letter){
        if (mQuery.equals("0.0")) mQuery="";
        boolean result;
        Operator op = getOperator(letter);
        if (op != null){
            if (op == Operator.MINUS && mQuery.isEmpty()){
                mQuery = mQuery + letter;
                result = true;
            }else if (canAppendOperator(mQuery)){
                mQuery = mQuery + letter;
                result = true;
            }else {
                result=false;
            }
        }else {
            if (letter.equals(DOT)) {
                if (canAppendDot(mQuery)){
                    mQuery = mQuery+letter;
                    result = true;
                }else {
                    result=false;
                }
            }else {
                if (true){
                    mQuery = mQuery+letter;
                    result = true;
                }else {
                    result=false;
                }
            }
        }
        return result;
    }

    public String backSpace(){
        if (mQuery.length() > 0){
            mQuery = mQuery.substring(0,mQuery.length()-1);
        }
        return mQuery;
    }

    private boolean canAppendDot(String query){
        boolean canAppend=true;
        for (int i=0;i<query.length();i++){
            String letter = query.substring(i,i+1);
            if (letter.equals(DOT)){
                canAppend=false;
            }else if (getOperator(letter) != null){
                canAppend=true;
            }
        }
        return canAppend;
    }

    private boolean canAppendOperator(String query){
        if (query.length()>0){
            String lastLetter = query.substring(query.length()-1);
            if (getOperator(lastLetter) != null) return false;
            if (lastLetter.equals(DOT)) return false;
            return true;
        }else {
            return false;
        }
    }

    public void clear(){
        mQuery = "";
    }

    public String getQuery(){
        return mQuery;
    }
    public void setQuery(String query){
        mQuery = query;
    }
}
