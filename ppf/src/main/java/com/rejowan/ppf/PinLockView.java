package com.rejowan.ppf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Random;

public class PinLockView extends RecyclerView {

    private static final int DEFAULT_PIN_LENGTH = 4;
    private static final int[] DEFAULT_KEY_SET = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

    private String mPin = "";
    private int mPinLength;
    private int mHorizontalSpacing, mVerticalSpacing;
    private int mTextColor, mDeleteButtonPressedColor;
    private int mTextSize, mButtonSize, mDeleteButtonSize;
    private Drawable mButtonBackgroundDrawable;
    private Drawable mDeleteButtonDrawable;
    private boolean mShowDeleteButton;

    private IndicatorDots mIndicatorDots;
    private PinLockAdapter mAdapter;
    private PinLockListener mPinLockListener;
    private CustomizationOptionsBundle mCustomizationOptionsBundle;
    private int[] mCustomKeySet;

    private final PinLockAdapter.OnNumberClickListener mOnNumberClickListener
            = new PinLockAdapter.OnNumberClickListener() {
        @Override
        public void onNumberClicked(int keyValue) {
            if (mPin.length() < getPinLength()) {
                mPin = mPin.concat(String.valueOf(keyValue));

                if (isIndicatorDotsAttached()) {
                    mIndicatorDots.updateDot(mPin.length());
                }

                if (mPin.length() == 1) {
                    mAdapter.setPinLength(mPin.length());
                    mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);
                }

                if (mPinLockListener != null) {
                    if (mPin.length() == mPinLength) {
                        mPinLockListener.onComplete(mPin);
                    } else {
                        mPinLockListener.onPinChange(mPin.length(), mPin);
                    }
                }
            } else {
                if (!isShowDeleteButton()) {
                    resetPinLockView();
                    mPin = mPin.concat(String.valueOf(keyValue));

                    if (isIndicatorDotsAttached()) {
                        mIndicatorDots.updateDot(mPin.length());
                    }

                    if (mPinLockListener != null) {
                        mPinLockListener.onPinChange(mPin.length(), mPin);
                    }

                } else {
                    if (mPinLockListener != null) {
                        mPinLockListener.onComplete(mPin);
                    }
                }
            }
        }
    };

    private final PinLockAdapter.OnDeleteClickListener mOnDeleteClickListener
            = new PinLockAdapter.OnDeleteClickListener() {
        @Override
        public void onDeleteClicked() {
            if (mPin.length() > 0) {
                mPin = mPin.substring(0, mPin.length() - 1);

                if (isIndicatorDotsAttached()) {
                    mIndicatorDots.updateDot(mPin.length());
                }

                if (mPin.length() == 0) {
                    mAdapter.setPinLength(mPin.length());
                    mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);
                }

                if (mPinLockListener != null) {
                    if (mPin.length() == 0) {
                        mPinLockListener.onEmpty();
                        clearInternalPin();
                    } else {
                        mPinLockListener.onPinChange(mPin.length(), mPin);
                    }
                }
            } else {
                if (mPinLockListener != null) {
                    mPinLockListener.onEmpty();
                }
            }
        }

        @Override
        public void onDeleteLongClicked() {
            resetPinLockView();
            if (mPinLockListener != null) {
                mPinLockListener.onEmpty();
            }
        }
    };

    public PinLockView(Context context) {
        super(context);
        init(null, 0);
    }

    public PinLockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PinLockView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attributeSet, int defStyle) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.PinLockView);

        try {
            mPinLength = typedArray.getInt(R.styleable.PinLockView_pinLength, DEFAULT_PIN_LENGTH);
            mHorizontalSpacing = (int) typedArray.getDimension(R.styleable.PinLockView_keyHorizontalSpacing, getDimensionInPx(getContext(), R.dimen.default_horizontal_spacing));
            mVerticalSpacing = (int) typedArray.getDimension(R.styleable.PinLockView_keyVerticalSpacing, getDimensionInPx(getContext(), R.dimen.default_vertical_spacing));
            mTextColor = typedArray.getColor(R.styleable.PinLockView_keyTextColor, getColor(getContext(), R.color.white));
            mTextSize = (int) typedArray.getDimension(R.styleable.PinLockView_keyTextSize, getDimensionInPx(getContext(), R.dimen.default_text_size));
            mButtonSize = (int) typedArray.getDimension(R.styleable.PinLockView_keyButtonSize, getDimensionInPx(getContext(), R.dimen.default_button_size));
            mDeleteButtonSize = (int) typedArray.getDimension(R.styleable.PinLockView_deleteButtonSize, getDimensionInPx(getContext(), R.dimen.default_delete_button_size));
            mButtonBackgroundDrawable = typedArray.getDrawable(R.styleable.PinLockView_keyBackgroundDrawable);
            mDeleteButtonDrawable = typedArray.getDrawable(R.styleable.PinLockView_deleteButtonDrawable);
            mShowDeleteButton = typedArray.getBoolean(R.styleable.PinLockView_showDeleteButton, true);
            mDeleteButtonPressedColor = typedArray.getColor(R.styleable.PinLockView_deleteButtonPressedColor, getColor(getContext(), R.color.greyish));
        } finally {
            typedArray.recycle();
        }

        mCustomizationOptionsBundle = new CustomizationOptionsBundle();
        mCustomizationOptionsBundle.setTextColor(mTextColor);
        mCustomizationOptionsBundle.setTextSize(mTextSize);
        mCustomizationOptionsBundle.setButtonSize(mButtonSize);
        mCustomizationOptionsBundle.setButtonBackgroundDrawable(mButtonBackgroundDrawable);
        mCustomizationOptionsBundle.setDeleteButtonDrawable(mDeleteButtonDrawable);
        mCustomizationOptionsBundle.setDeleteButtonSize(mDeleteButtonSize);
        mCustomizationOptionsBundle.setShowDeleteButton(mShowDeleteButton);
        mCustomizationOptionsBundle.setDeleteButtonPressesColor(mDeleteButtonPressedColor);

        initView();
    }

    private void initView() {
        setLayoutManager(new LTRGridLayoutManager(getContext(), 3));

        mAdapter = new PinLockAdapter(getContext());
        mAdapter.setOnItemClickListener(mOnNumberClickListener);
        mAdapter.setOnDeleteClickListener(mOnDeleteClickListener);
        mAdapter.setCustomizationOptions(mCustomizationOptionsBundle);
        setAdapter(mAdapter);


        addItemDecoration(new ItemSpaceDecoration(mHorizontalSpacing, mVerticalSpacing, 3, false));
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    /**
     * Sets a {@link PinLockListener} to the to listen to pin update events
     *
     * @param pinLockListener the listener
     */
    public void setPinLockListener(PinLockListener pinLockListener) {
        this.mPinLockListener = pinLockListener;
    }

    /**
     * Get the length of the current pin length
     *
     * @return the length of the pin
     */
    public int getPinLength() {
        return mPinLength;
    }

    /**
     * Sets the pin length dynamically
     *
     * @param pinLength the pin length
     */
    public void setPinLength(int pinLength) {
        this.mPinLength = pinLength;

        if (isIndicatorDotsAttached()) {
            mIndicatorDots.setCount(pinLength);
        }
    }

    /**
     * Get the text color in the buttons
     *
     * @return the text color
     */
    public int getTextColor() {
        return mTextColor;
    }


    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        mCustomizationOptionsBundle.setTextColor(textColor);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Get the size of the text in the buttons
     *
     * @return the size of the text in pixels
     */
    public int getTextSize() {
        return mTextSize;
    }

    /**
     * Set the size of text in pixels
     *
     * @param textSize the text size in pixels
     */
    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
        mCustomizationOptionsBundle.setTextSize(textSize);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Get the size of the pin buttons
     *
     * @return the size of the button in pixels
     */
    public int getButtonSize() {
        return mButtonSize;
    }

    /**
     * Set the size of the pin buttons dynamically
     *
     * @param buttonSize the button size
     */
    public void setButtonSize(int buttonSize) {
        this.mButtonSize = buttonSize;
        mCustomizationOptionsBundle.setButtonSize(buttonSize);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Get the current background drawable of the buttons, can be null
     *
     * @return the background drawable
     */
    public Drawable getButtonBackgroundDrawable() {
        return mButtonBackgroundDrawable;
    }

    /**
     * Set the background drawable of the buttons dynamically
     *
     * @param buttonBackgroundDrawable the background drawable
     */
    public void setButtonBackgroundDrawable(Drawable buttonBackgroundDrawable) {
        this.mButtonBackgroundDrawable = buttonBackgroundDrawable;
        mCustomizationOptionsBundle.setButtonBackgroundDrawable(buttonBackgroundDrawable);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Get the drawable of the delete button
     *
     * @return the delete button drawable
     */
    public Drawable getDeleteButtonDrawable() {
        return mDeleteButtonDrawable;
    }

    /**
     * Set the drawable of the delete button dynamically
     *
     * @param deleteBackgroundDrawable the delete button drawable
     */
    public void setDeleteButtonDrawable(Drawable deleteBackgroundDrawable) {
        this.mDeleteButtonDrawable = deleteBackgroundDrawable;
        mCustomizationOptionsBundle.setDeleteButtonDrawable(deleteBackgroundDrawable);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Get the delete button size in pixels
     *
     * @return size in pixels
     */
    public int getDeleteButtonSize() {
        return mDeleteButtonSize;
    }

    /**
     * Set the size of the delete button in pixels
     *
     * @param deleteButtonSize size in pixels
     */
    public void setDeleteButtonSize(int deleteButtonSize) {
        this.mDeleteButtonSize = deleteButtonSize;
        mCustomizationOptionsBundle.setDeleteButtonSize(deleteButtonSize);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Is the delete button shown
     *
     * @return returns true if shown, false otherwise
     */
    public boolean isShowDeleteButton() {
        return mShowDeleteButton;
    }

    /**
     * Dynamically set if the delete button should be shown
     *
     * @param showDeleteButton true if the delete button should be shown, false otherwise
     */
    public void setShowDeleteButton(boolean showDeleteButton) {
        this.mShowDeleteButton = showDeleteButton;
        mCustomizationOptionsBundle.setShowDeleteButton(showDeleteButton);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Get the delete button pressed/focused state color
     *
     * @return color of the button
     */
    public int getDeleteButtonPressedColor() {
        return mDeleteButtonPressedColor;
    }

    /**
     * Set the pressed/focused state color of the delete button
     *
     * @param deleteButtonPressedColor the color of the delete button
     */
    public void setDeleteButtonPressedColor(int deleteButtonPressedColor) {
        this.mDeleteButtonPressedColor = deleteButtonPressedColor;
        mCustomizationOptionsBundle.setDeleteButtonPressesColor(deleteButtonPressedColor);
        mAdapter.notifyDataSetChanged();
    }

    public int[] getCustomKeySet() {
        return mCustomKeySet;
    }

    public void setCustomKeySet(int[] customKeySet) {
        this.mCustomKeySet = customKeySet;

        if (mAdapter != null) {
            mAdapter.setKeyValues(customKeySet);
        }
    }

    public void enableLayoutShuffling() {
        this.mCustomKeySet = shuffle(DEFAULT_KEY_SET);

        if (mAdapter != null) {
            mAdapter.setKeyValues(mCustomKeySet);
        }
    }

    private void clearInternalPin() {
        mPin = "";
    }

    /**
     * Resets the {@link PinLockView}, clearing the entered pin
     * and resetting the {@link IndicatorDots} if attached
     */
    public void resetPinLockView() {

        clearInternalPin();

        mAdapter.setPinLength(mPin.length());
        mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);

        if (mIndicatorDots != null) {
            mIndicatorDots.updateDot(mPin.length());
        }
    }

    /**
     * Returns true if {@link IndicatorDots} are attached to {@link PinLockView}
     *
     * @return true if attached, false otherwise
     */
    public boolean isIndicatorDotsAttached() {
        return mIndicatorDots != null;
    }

    /**
     * Attaches {@link IndicatorDots} to {@link PinLockView}
     *
     * @param mIndicatorDots the view to attach
     */
    public void attachIndicatorDots(IndicatorDots mIndicatorDots) {
        this.mIndicatorDots = mIndicatorDots;
    }


    public static class PinLockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int VIEW_TYPE_NUMBER = 0;
        private static final int VIEW_TYPE_DELETE = 1;

        private final Context mContext;
        private CustomizationOptionsBundle mCustomizationOptionsBundle;
        private OnNumberClickListener mOnNumberClickListener;
        private OnDeleteClickListener mOnDeleteClickListener;
        private int mPinLength;

        private int[] mKeyValues;

        public PinLockAdapter(Context context) {
            this.mContext = context;
            this.mKeyValues = getAdjustKeyValues(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0});
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_NUMBER) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_number_item, parent, false);
                return new NumberViewHolder(view);
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_delete_item, parent, false);
                return new DeleteViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder.getItemViewType() == VIEW_TYPE_NUMBER) {
                NumberViewHolder vh1 = (NumberViewHolder) holder;
                configureNumberButtonHolder(vh1, position);
            } else if (holder.getItemViewType() == VIEW_TYPE_DELETE) {
                DeleteViewHolder vh2 = (DeleteViewHolder) holder;
                configureDeleteButtonHolder(vh2);
            }
        }

        private void configureNumberButtonHolder(NumberViewHolder holder, int position) {
            if (holder != null) {
                if (position == 9) {
                    holder.mNumberButton.setVisibility(View.GONE);
                } else {
                    holder.mNumberButton.setText(String.valueOf(mKeyValues[position]));
                    holder.mNumberButton.setVisibility(View.VISIBLE);
                    holder.mNumberButton.setTag(mKeyValues[position]);
                }

                if (mCustomizationOptionsBundle != null) {
                    holder.mNumberButton.setTextColor(mCustomizationOptionsBundle.getTextColor());
                    if (mCustomizationOptionsBundle.getButtonBackgroundDrawable() != null) {
                        holder.mNumberButton.setBackground(
                                mCustomizationOptionsBundle.getButtonBackgroundDrawable());
                    }
                    holder.mNumberButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            mCustomizationOptionsBundle.getTextSize());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            mCustomizationOptionsBundle.getButtonSize(),
                            mCustomizationOptionsBundle.getButtonSize());
                    holder.mNumberButton.setLayoutParams(params);
                }
            }
        }

        private void configureDeleteButtonHolder(DeleteViewHolder holder) {
            if (holder != null) {
                if (mCustomizationOptionsBundle.isShowDeleteButton() && mPinLength > 0) {
                    holder.mButtonImage.setVisibility(View.VISIBLE);
                    if (mCustomizationOptionsBundle.getDeleteButtonDrawable() != null) {
                        holder.mButtonImage.setImageDrawable(mCustomizationOptionsBundle.getDeleteButtonDrawable());
                    }
                    holder.mButtonImage.setColorFilter(mCustomizationOptionsBundle.getTextColor(),
                            PorterDuff.Mode.SRC_ATOP);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            mCustomizationOptionsBundle.getDeleteButtonSize(),
                            mCustomizationOptionsBundle.getDeleteButtonSize());
                    holder.mButtonImage.setLayoutParams(params);
                } else {
                    holder.mButtonImage.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public int getItemCount() {
            return 12;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return VIEW_TYPE_DELETE;
            }
            return VIEW_TYPE_NUMBER;
        }

        public int getPinLength() {
            return mPinLength;
        }

        public void setPinLength(int pinLength) {
            this.mPinLength = pinLength;
        }

        public int[] getKeyValues() {
            return mKeyValues;
        }

        public void setKeyValues(int[] keyValues) {
            this.mKeyValues = getAdjustKeyValues(keyValues);
            notifyDataSetChanged();
        }

        private int[] getAdjustKeyValues(int[] keyValues) {
            int[] adjustedKeyValues = new int[keyValues.length + 1];
            for (int i = 0; i < keyValues.length; i++) {
                if (i < 9) {
                    adjustedKeyValues[i] = keyValues[i];
                } else {
                    adjustedKeyValues[i] = -1;
                    adjustedKeyValues[i + 1] = keyValues[i];
                }
            }
            return adjustedKeyValues;
        }

        public OnNumberClickListener getOnItemClickListener() {
            return mOnNumberClickListener;
        }

        public void setOnItemClickListener(OnNumberClickListener onNumberClickListener) {
            this.mOnNumberClickListener = onNumberClickListener;
        }

        public OnDeleteClickListener getOnDeleteClickListener() {
            return mOnDeleteClickListener;
        }

        public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
            this.mOnDeleteClickListener = onDeleteClickListener;
        }

        public CustomizationOptionsBundle getCustomizationOptions() {
            return mCustomizationOptionsBundle;
        }

        public void setCustomizationOptions(CustomizationOptionsBundle customizationOptionsBundle) {
            this.mCustomizationOptionsBundle = customizationOptionsBundle;
        }

        public class NumberViewHolder extends RecyclerView.ViewHolder {
            Button mNumberButton;

            public NumberViewHolder(final View itemView) {
                super(itemView);
                mNumberButton = itemView.findViewById(R.id.button);
                mNumberButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnNumberClickListener != null) {
                            mOnNumberClickListener.onNumberClicked((Integer) v.getTag());
                        }
                    }
                });
            }
        }

        public class DeleteViewHolder extends RecyclerView.ViewHolder {
            LinearLayout mDeleteButton;
            ImageView mButtonImage;

            @SuppressLint("ClickableViewAccessibility")
            public DeleteViewHolder(final View itemView) {
                super(itemView);
                mDeleteButton = itemView.findViewById(R.id.button);
                mButtonImage = itemView.findViewById(R.id.buttonImage);

                if (mCustomizationOptionsBundle.isShowDeleteButton() && mPinLength > 0) {
                    mDeleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnDeleteClickListener != null) {
                                mOnDeleteClickListener.onDeleteClicked();
                            }
                        }
                    });

                    mDeleteButton.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if (mOnDeleteClickListener != null) {
                                mOnDeleteClickListener.onDeleteLongClicked();
                            }
                            return true;
                        }
                    });

                    mDeleteButton.setOnTouchListener(new View.OnTouchListener() {
                        private Rect rect;

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                mButtonImage.setColorFilter(mCustomizationOptionsBundle
                                        .getDeleteButtonPressesColor());
                                rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                            }
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                mButtonImage.clearColorFilter();
                            }
                            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                                if (!rect.contains(v.getLeft() + (int) event.getX(),
                                        v.getTop() + (int) event.getY())) {
                                    mButtonImage.clearColorFilter();
                                }
                            }
                            return false;
                        }
                    });
                }
            }
        }

        public interface OnNumberClickListener {
            void onNumberClicked(int keyValue);
        }

        public interface OnDeleteClickListener {
            void onDeleteClicked();

            void onDeleteLongClicked();
        }
    }

    public interface PinLockListener {


        void onComplete(String pin);


        void onEmpty();


        void onPinChange(int pinLength, String intermediatePin);
    }


    public static int getColor(Context context, @ColorRes int id) {
        return ContextCompat.getColor(context, id);
    }

    public static float getDimensionInPx(Context context, @DimenRes int id) {
        return context.getResources().getDimension(id);
    }

    public static Drawable getDrawable(Context context, @DrawableRes int id) {
        return ContextCompat.getDrawable(context, id);
    }

    static int[] shuffle(int[] array) {
        int length = array.length;
        Random random = new Random();
        random.nextInt();

        for (int i = 0; i < length; i++) {
            int change = i + random.nextInt(length - i);
            swap(array, i, change);
        }
        return array;
    }

    private static void swap(int[] array, int index, int change) {
        int temp = array[index];
        array[index] = array[change];
        array[change] = temp;
    }

    public class LTRGridLayoutManager extends GridLayoutManager {

        public LTRGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public LTRGridLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
        }

        public LTRGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
            super(context, spanCount, orientation, reverseLayout);
        }

        @Override
        protected boolean isLayoutRTL() {
            return false;
        }
    }

    public class ItemSpaceDecoration extends RecyclerView.ItemDecoration {

        private final int mHorizontalSpaceWidth;
        private final int mVerticalSpaceHeight;
        private final int mSpanCount;
        private final boolean mIncludeEdge;

        public ItemSpaceDecoration(int horizontalSpaceWidth, int verticalSpaceHeight, int spanCount, boolean includeEdge) {
            this.mHorizontalSpaceWidth = horizontalSpaceWidth;
            this.mVerticalSpaceHeight = verticalSpaceHeight;
            this.mSpanCount = spanCount;
            this.mIncludeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            int position = parent.getChildAdapterPosition(view);
            int column = position % mSpanCount;

            if (mIncludeEdge) {
                outRect.left = mHorizontalSpaceWidth - column * mHorizontalSpaceWidth / mSpanCount;
                outRect.right = (column + 1) * mHorizontalSpaceWidth / mSpanCount;

                if (position < mSpanCount) {
                    outRect.top = mVerticalSpaceHeight;
                }
                outRect.bottom = mVerticalSpaceHeight;
            } else {
                outRect.left = column * mHorizontalSpaceWidth / mSpanCount;
                outRect.right = mHorizontalSpaceWidth - (column + 1) * mHorizontalSpaceWidth / mSpanCount;
                if (position >= mSpanCount) {
                    outRect.top = mVerticalSpaceHeight;
                }
            }
        }
    }


    public class CustomizationOptionsBundle {

        private int textColor;
        private int textSize;
        private int buttonSize;
        private Drawable buttonBackgroundDrawable;
        private Drawable deleteButtonDrawable;
        private int deleteButtonSize;
        private boolean showDeleteButton;
        private int deleteButtonPressesColor;

        public CustomizationOptionsBundle() {
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        public int getTextSize() {
            return textSize;
        }

        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }

        public int getButtonSize() {
            return buttonSize;
        }

        public void setButtonSize(int buttonSize) {
            this.buttonSize = buttonSize;
        }

        public Drawable getButtonBackgroundDrawable() {
            return buttonBackgroundDrawable;
        }

        public void setButtonBackgroundDrawable(Drawable buttonBackgroundDrawable) {
            this.buttonBackgroundDrawable = buttonBackgroundDrawable;
        }

        public Drawable getDeleteButtonDrawable() {
            return deleteButtonDrawable;
        }

        public void setDeleteButtonDrawable(Drawable deleteButtonDrawable) {
            this.deleteButtonDrawable = deleteButtonDrawable;
        }

        public int getDeleteButtonSize() {
            return deleteButtonSize;
        }

        public void setDeleteButtonSize(int deleteButtonSize) {
            this.deleteButtonSize = deleteButtonSize;
        }

        public boolean isShowDeleteButton() {
            return showDeleteButton;
        }

        public void setShowDeleteButton(boolean showDeleteButton) {
            this.showDeleteButton = showDeleteButton;
        }

        public int getDeleteButtonPressesColor() {
            return deleteButtonPressesColor;
        }

        public void setDeleteButtonPressesColor(int deleteButtonPressesColor) {
            this.deleteButtonPressesColor = deleteButtonPressesColor;
        }
    }
}
