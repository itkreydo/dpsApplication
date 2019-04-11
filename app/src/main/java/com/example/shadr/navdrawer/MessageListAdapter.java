package com.example.shadr.navdrawer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shadr.navdrawer.fragment.FragmentGallery;
import com.example.shadr.navdrawer.models.Message;

import java.util.List;
public class MessageListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Message> mMessageList;

    public MessageListAdapter(Context context, List<Message> messageList) {
        mContext = context;
        mMessageList = messageList;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Message message = (Message) mMessageList.get(position);

        if (message.getType()==Message.TYPE_MESSAGE_SENT) {
            // If the current user is the sender of the message
            return Message.TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return Message.TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;

        if (viewType == Message.TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_message_sent, viewGroup, false);
            return new SentMessageHolder(view);
        } else if (viewType == Message.TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_message_received, viewGroup, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Message message = (Message) mMessageList.get(position);

        switch (viewHolder.getItemViewType()) {
            case Message.TYPE_MESSAGE_SENT:
                ((SentMessageHolder) viewHolder).bind(message);
                break;
            case Message.TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) viewHolder).bind(message);
        }
    }


    public class ReceivedMessageHolder  extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;
        LinearLayout mainlayer;
        ImageView messageImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
            messageImage = itemView.findViewById(R.id.image_message_body);
            mainlayer = itemView.findViewById(R.id.container_message);
        }

        void bind(Message message) {
            if(message.getBitmap()!=null) {
                messageImage.setImageBitmap(getRoundedCornerBitmap(message.getBitmap(), dp_to_px(20)));
                messageText.setVisibility(View.GONE);
                messageImage.setVisibility(View.VISIBLE);
            }
            else {
                messageText.setText(message.getMessage());
                messageImage.setVisibility(View.GONE);
                messageText.setVisibility(View.VISIBLE);
            }
            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getDate_time());
            nameText.setText(message.getSender().getNickname());

            // Insert the profile image from the URL into the ImageView.
            //Utils.displayRoundImageFromUrl(getContext(), message.getSender().getProfileUrl(), profileImage);
        }
    }

    public class SentMessageHolder  extends RecyclerView.ViewHolder{
        TextView messageText, timeText;
        ImageView messageImage;
        LinearLayout mainlayer;

        SentMessageHolder(View itemView) {
            super(itemView);
            mainlayer = itemView.findViewById(R.id.container_message);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            messageImage = itemView.findViewById(R.id.image_message_body);
        }

        void bind(Message message) {
            if(message.getBitmap()!=null) {
                messageImage.setImageBitmap(getRoundedCornerBitmap(message.getBitmap(), dp_to_px(20)));
                messageText.setVisibility(View.GONE);
                messageImage.setVisibility(View.VISIBLE);
            }
            else {
                messageText.setText(message.getMessage());
                messageImage.setVisibility(View.GONE);
                messageText.setVisibility(View.VISIBLE);
            }
            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getDate_time());
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    private int dp_to_px(int dp) {

        Resources r = mContext.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return px;
    }
}
