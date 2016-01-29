package com.hck.yanghua.view;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hck.yanghua.R;

public class MyEditextView extends EditText {

	public MyEditextView(Context context) {
		super(context);
	}

	public MyEditextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {

		setListener();
	}

	private void setListener() {
		this.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence editable, int start,
					int before, int count) {
				int index = getSelectionStart() - 1;
				if (index > 0) {
					if (isEmojiCharacter(editable.charAt(index))) {
						Editable edit = getText();
						edit.delete(index, index + 1);
					}
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private static boolean isEmojiCharacter(char codePoint) {
		return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
				|| (codePoint == 0xD) || ((codePoint >= 0x20) && codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

}
