package com.flowcamp.tab.ui.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flowcamp.tab.R;
import com.flowcamp.tab.model.Board;
import com.flowcamp.tab.model.Choice;
import com.flowcamp.tab.model.Player;

public class TicTacToeFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private Board board;
    private ViewGroup buttonGrid;
    private View winnerPlayerViewGroup;
    private TextView winnerPlayerLabel;
    private Button btnReset;
    private Button[][] buttons;


    public TicTacToeFragment() {
    }

    public static TicTacToeFragment newInstance(int position) {
        TicTacToeFragment fragment = new TicTacToeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tic_tac_toe, container, false);
        winnerPlayerLabel = root.findViewById(R.id.winnerPlayerLabel);
        winnerPlayerViewGroup = root.findViewById(R.id.winnerPlayerViewGroup);
        buttonGrid = root.findViewById(R.id.buttonGrid);
        btnReset = root.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
        board = new Board();

        int x, y;
        x = (int) (Math.random() * 3);
        y = (int) (Math.random() * 3);
        board.mark(x, y, Player.X);

        buttons = new Button[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new Button(getActivity());
                Button button = buttons[i][j];
                button.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
                button.setTextSize(50);

                if (i == x && j == y) {
                    button.setText(Player.X.toString());
                }

                button.setTag(Integer.toString(i) + j);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCellClicked(view);
                    }
                });

                buttonGrid.addView(button);
            }
        }

        return root;
    }

    public void onCellClicked(View v) {
        Button button = (Button) v;

        String tag = button.getTag().toString();
        int row = Integer.parseInt(tag.substring(0, 1));
        int col = Integer.parseInt(tag.substring(1, 2));

        Player playerThatMoved = board.mark(row, col, Player.O);

        if (playerThatMoved != null) {
            button.setText(playerThatMoved.toString());

            if (board.getWinner() != null) {
                winnerPlayerLabel.setText(playerThatMoved.toString());
                winnerPlayerViewGroup.setVisibility(View.VISIBLE);
            } else {
                Choice choice = board.com_choice();
                board.mark(choice.getRow(), choice.getColumn(), Player.X);
                buttons[choice.getRow()][choice.getColumn()].setText(Player.X.toString());
                if (board.getWinner() != null) {
                    winnerPlayerLabel.setText(Player.X.toString());
                    winnerPlayerViewGroup.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void reset() {
        winnerPlayerViewGroup.setVisibility(View.GONE);
        winnerPlayerLabel.setText("");

        board.restart();
        for (int i = 0; i < buttonGrid.getChildCount(); i++) {
            ((Button) buttonGrid.getChildAt(i)).setText("");
        }
        int x = (int) (Math.random() * 3);
        int y = (int) (Math.random() * 3);
        board.mark(x, y, Player.X);
        buttons[x][y].setText(Player.X.toString());
    }
}