package amarnehsoft.com.debits.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.controllers.CalculatorController;
import amarnehsoft.com.debits.utils.NumberUtils;

public class CalculatorActivity extends AppCompatActivity {
    public static final String ARG_RESULT="result";
    private static final String ARG_TITLE="title";
    private static final String ARG_QUERY="query";

    private TextView txtQuery,txtResult;
    private Button btnClear,btnBackSpace,btnMul,btn1,btn2,btn3,btnDevide,btn4,btn5,btn6,btnPlus,btn7,btn8,btn9,btnMinus,btn0,btnDot,btnEqual,btnDone;

    public static Intent newIntent(Context context,String title,String query){
        Intent intent = new Intent(context,CalculatorActivity.class);
        intent.putExtra(ARG_TITLE,title);
        intent.putExtra(ARG_QUERY,query);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        CalculatorController.getInstance().clear();

        txtQuery = (TextView)findViewById(R.id.txtQuery);
        txtResult = (TextView)findViewById(R.id.txtResult);

        btnClear = (Button)findViewById(R.id.btnClear);
        btnBackSpace= (Button)findViewById(R.id.btnBackSpace);
        btnMul = (Button)findViewById(R.id.btnMultiply);
        btnDevide = (Button)findViewById(R.id.btnDevide);
        btnPlus = (Button)findViewById(R.id.btnPlus);
        btnMinus = (Button)findViewById(R.id.btnMinus);
        btn1=(Button)findViewById(R.id.btn1);
        btn2=(Button)findViewById(R.id.btn2);
        btn3=(Button)findViewById(R.id.btn3);
        btn4=(Button)findViewById(R.id.btn4);
        btn5=(Button)findViewById(R.id.btn5);
        btn6=(Button)findViewById(R.id.btn6);
        btn7=(Button)findViewById(R.id.btn7);
        btn8=(Button)findViewById(R.id.btn8);
        btn9=(Button)findViewById(R.id.btn9);
        btn0=(Button)findViewById(R.id.btn0);
        btnEqual= (Button)findViewById(R.id.btnEqual);
        btnDot = (Button)findViewById(R.id.btnDot);
        btnDone = (Button)findViewById(R.id.btnDone);

        String title = getIntent().getStringExtra(ARG_TITLE);
        if (title == null) title = getString(R.string.theCalculator);


        String query = getIntent().getStringExtra(ARG_QUERY);
        if (query != null){
            CalculatorController.getInstance().setQuery(query);
            refresh();
        }

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorController.getInstance().append("0");
                refresh();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorController.getInstance().append("1");
                refresh();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorController.getInstance().append("2");
                refresh();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorController.getInstance().append("3");
                refresh();
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorController.getInstance().append("4");
                refresh();
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorController.getInstance().append("5");
                refresh();
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorController.getInstance().append("6");
                refresh();
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorController.getInstance().append("7");
                refresh();
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorController.getInstance().append("8");
                refresh();
            }
        });

        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorController.getInstance().append("9");
                refresh();
            }
        });

        btnDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorController.getInstance().append(CalculatorController.DOT);
                refresh();
            }
        });

        btnMul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorController.getInstance().append(CalculatorController.Operator.MULTIPLY.toString());
                refresh();
            }
        });

        btnDevide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorController.getInstance().append(CalculatorController.Operator.DIVID.toString());
                refresh();
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorController.getInstance().append(CalculatorController.Operator.PLUS.toString());
                refresh();
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorController.getInstance().append(CalculatorController.Operator.MINUS.toString());
                refresh();
            }
        });

        btnBackSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorController.getInstance().backSpace();
                refresh();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorController.getInstance().clear();
                refresh();
            }
        });

        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorController.getInstance().setQuery(CalculatorController.getInstance().calculate()+"");
                refresh();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorController.getInstance().clear();
                Intent intent = new Intent();
                double result = NumberUtils.getDouble(txtResult.getText().toString());
                intent.putExtra(ARG_RESULT, NumberUtils.Round(result));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    private void refresh(){
        txtQuery.setText(CalculatorController.getInstance().getQuery());
        txtResult.setText(CalculatorController.getInstance().calculate() +"");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title",getTitle().toString());
        outState.putString("query",txtQuery.getText().toString());
        outState.putString("result",txtResult.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setTitle(savedInstanceState.getString("title"));
        txtQuery.setText(savedInstanceState.getString("query"));
        txtResult.setText(savedInstanceState.getString("result"));
    }
}
