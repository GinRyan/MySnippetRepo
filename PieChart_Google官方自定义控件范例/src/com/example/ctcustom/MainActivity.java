package com.example.ctcustom;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.example.android.customviews.charting.PieChart;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Resources res = getResources();

		setContentView(R.layout.main);
		final PieChart pie = (PieChart) this.findViewById(R.id.Pie);
		pie.addItem("Agamemnon", 2, res.getColor(R.color.seafoam));
		pie.addItem("Bocephus", 3.5f, res.getColor(R.color.chartreuse));
		pie.addItem("Calliope", 2.5f, res.getColor(R.color.emerald));
		pie.addItem("Daedalus", 3, res.getColor(R.color.bluegrass));
		pie.addItem("Euripides", 1, res.getColor(R.color.turquoise));
		pie.addItem("Ganymede", 3, res.getColor(R.color.slate));

		((Button) findViewById(R.id.Reset)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				pie.setCurrentItem(0);
			}
		});
	}

}
