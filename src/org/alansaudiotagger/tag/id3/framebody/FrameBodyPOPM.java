/*
 *  MusicTag Copyright (C)2003,2004
 *
 *  This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 *  General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 *  or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 *  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 *  you can get a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.alansaudiotagger.tag.id3.framebody;

import org.alansaudiotagger.tag.InvalidTagException;
import org.alansaudiotagger.tag.datatype.DataTypes;
import org.alansaudiotagger.tag.datatype.NumberFixedLength;
import org.alansaudiotagger.tag.datatype.NumberVariableLength;
import org.alansaudiotagger.tag.datatype.StringNullTerminated;
import org.alansaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;

/**
 * Popularimeter frame.
 * <p/>
 * <p/>
 * The purpose of this frame is to specify how good an audio file is.
 * Many interesting applications could be found to this frame such as a
 * playlist that features better audiofiles more often than others or it
 * could be used to profile a person's taste and find other 'good' files
 * by comparing people's profiles. The frame is very simple. It contains
 * the email address to the user, one rating byte and a four byte play
 * counter, intended to be increased with one for every time the file is
 * played. The email is a terminated string. The rating is 1-255 where
 * 1 is worst and 255 is best. 0 is unknown. If no personal counter is
 * wanted it may be omitted. When the counter reaches all one's, one
 * byte is inserted in front of the counter thus making the counter
 * eight bits bigger in the same away as the play counter ("PCNT").
 * There may be more than one "POPM" frame in each tag, but only one
 * with the same email address.
 * </p><p><table border=0 width="70%">
 * <tr><td colspan=2> &lt;Header for 'Popularimeter', ID: "POPM"&gt;</td></tr>
 * <tr><td>Email to user</td><td>&lt;text string&gt; $00</td></tr>
 * <tr><td>Rating       </td><td>$xx                    </td></tr>
 * <tr><td>Counter      </td><td>$xx xx xx xx (xx ...)  </td></tr>
 * </table></p>
 * <p/>
 * <p>For more details, please refer to the ID3 specifications:
 * <ul>
 * <li><a href="http://www.id3.org/id3v2.3.0.txt">ID3 v2.3.0 Spec</a>
 * </ul>
 *
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id: FrameBodyPOPM.java,v 1.15 2008/07/21 10:45:43 paultaylor Exp $
 * @todo : Counter should be optional, whereas we always expect it although allow a size of zero
 * needs testing.
 */
public class FrameBodyPOPM extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{
    private static final int RATING_FIELD_SIZE = 1;
    private static final int COUNTER_MINIMUM_FIELD_SIZE = 0;

    /**
     * Creates a new FrameBodyPOPM datatype.
     */
    public FrameBodyPOPM()
    {
        this.setObjectValue(DataTypes.OBJ_EMAIL, "");
        this.setObjectValue(DataTypes.OBJ_RATING, (long) 0);
        this.setObjectValue(DataTypes.OBJ_COUNTER, (long) 0);
    }

    public FrameBodyPOPM(FrameBodyPOPM body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyPOPM datatype.
     *
     * @param emailToUser
     * @param rating
     * @param counter
     */
    public FrameBodyPOPM(String emailToUser, long rating, long counter)
    {
        this.setObjectValue(DataTypes.OBJ_EMAIL, emailToUser);
        this.setObjectValue(DataTypes.OBJ_RATING, rating);
        this.setObjectValue(DataTypes.OBJ_COUNTER, counter);
    }

    /**
     * Creates a new FrameBodyPOPM datatype.
     *
     * @throws InvalidTagException if unable to create framebody from buffer
     */
    public FrameBodyPOPM(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

    /**
     * @param description
     */
    public void setEmailToUser(String description)
    {
        setObjectValue(DataTypes.OBJ_EMAIL, description);
    }

    /**
     * @return the memail of the user who rated this
     */
    public String getEmailToUser()
    {
        return (String) getObjectValue(DataTypes.OBJ_EMAIL);
    }

    /**
     * @return the rating given to this file
     */
    public long getRating()
    {
        return ((Number) getObjectValue(DataTypes.OBJ_RATING)).longValue();
    }

    /**
     * Set the rating given to this file
     *
     * @param rating
     */
    public void setRating(long rating)
    {
        setObjectValue(DataTypes.OBJ_RATING, rating);
    }

    /**
     * @return the play count of this file
     */
    public long getCounter()
    {
        return ((Number) getObjectValue(DataTypes.OBJ_COUNTER)).longValue();
    }

    /**
     * Set the play counter of this file
     *
     * @param counter
     */
    public void setCounter(long counter)
    {
        setObjectValue(DataTypes.OBJ_COUNTER, counter);
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_POPULARIMETER;
    }


    /**
     *
     */
    protected void setupObjectList()
    {
        objectList.add(new StringNullTerminated(DataTypes.OBJ_EMAIL, this));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_RATING, this, RATING_FIELD_SIZE));
        objectList.add(new NumberVariableLength(DataTypes.OBJ_COUNTER, this, COUNTER_MINIMUM_FIELD_SIZE));
    }
}