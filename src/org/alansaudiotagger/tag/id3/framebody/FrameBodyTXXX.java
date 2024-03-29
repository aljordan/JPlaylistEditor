/**
 *  @author : Paul Taylor
 *  @author : Eric Farng
 *
 *  Version @version:$Id: FrameBodyTXXX.java,v 1.18 2008/07/21 10:45:46 paultaylor Exp $
 *
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
 *
 * Description:
 *
 */
package org.alansaudiotagger.tag.id3.framebody;

import org.alansaudiotagger.tag.InvalidTagException;
import org.alansaudiotagger.tag.datatype.DataTypes;
import org.alansaudiotagger.tag.datatype.NumberHashMap;
import org.alansaudiotagger.tag.datatype.TextEncodedStringNullTerminated;
import org.alansaudiotagger.tag.datatype.TextEncodedStringSizeTerminated;
import org.alansaudiotagger.tag.id3.ID3TextEncodingConversion;
import org.alansaudiotagger.tag.id3.ID3v24Frames;
import org.alansaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;


/**
 * User defined text information frame
 * <p/>
 * This frame is intended for one-string text information concerning the
 * audio file in a similar way to the other "T"-frames. The frame body
 * consists of a description of the string, represented as a terminated
 * string, followed by the actual string. There may be more than one
 * "TXXX" frame in each tag, but only one with the same description.
 * <p/>
 * <Header for 'User defined text information frame', ID: "TXXX">
 * Text encoding     $xx
 * Description       <text string according to encoding> $00 (00)
 * Value             <text string according to encoding>
 */
public class FrameBodyTXXX extends AbstractFrameBodyTextInfo implements ID3v24FrameBody, ID3v23FrameBody
{
    public static final String MUSIC_BRAINZ_ARTISTID = "MusicBrainz Artist Id";
    public static final String MUSIC_BRAINZ_ALBUM_ARTISTID = "MusicBrainz Album Artist Id";
    public static final String MUSIC_BRAINZ_ALBUMID = "MusicBrainz Album Id";
    public static final String MUSIC_BRAINZ_DISCID = "MusicBrainz Disc Id";
    public static final String MUSICBRAINZ_ALBUM_TYPE = "MusicBrainz Album Type";
    public static final String MUSICBRAINZ_ALBUM_STATUS = "MusicBrainz Album Status";
    public static final String MUSICBRAINZ_ALBUM_COUNTRY = "MusicBrainz Album Release Country";
    public static final String AMAZON_ASIN = "ASIN";
    public static final String MUSICIP_ID = "MusicIP PUID";
    public static final String BARCODE = "BARCODE";
    public static final String CATALOG_NO = "CATALOGNUMBER";

    /**
     * Creates a new FrameBodyTXXX datatype.
     */
    public FrameBodyTXXX()
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1);
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, "");
        this.setObjectValue(DataTypes.OBJ_TEXT, "");

    }

    public FrameBodyTXXX(FrameBodyTXXX body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyTXXX datatype.
     *
     * @param textEncoding
     * @param description
     * @param text
     */
    public FrameBodyTXXX(byte textEncoding, String description, String text)
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
        this.setObjectValue(DataTypes.OBJ_TEXT, text);
    }

    /**
     * Creates a new FrameBodyTXXX datatype.
     *
     * @throws InvalidTagException
     */
    public FrameBodyTXXX(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

    /**
     * Set the desciption field
     *
     * @param description
     */
    public void setDescription(String description)
    {
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
    }

    /**
     * @return the description field
     */
    public String getDescription()
    {
        return (String) getObjectValue(DataTypes.OBJ_DESCRIPTION);
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_USER_DEFINED_INFO;
    }

    /**
     * Because TXXX frames also have a text encoded description we need to check this as well.     *
     */
    public void write(ByteArrayOutputStream tagBuffer)
    {
        //Ensure valid for type
        setTextEncoding(ID3TextEncodingConversion.getTextEncoding(getHeader(), getTextEncoding()));

        //Ensure valid for description
        if (((TextEncodedStringNullTerminated) getObject(DataTypes.OBJ_DESCRIPTION)).canBeEncoded() == false)
        {
            this.setTextEncoding(ID3TextEncodingConversion.getUnicodeTextEncoding(getHeader()));
        }
        super.write(tagBuffer);
    }

    /**
     * This is different to other text Frames
     */
    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new TextEncodedStringNullTerminated(DataTypes.OBJ_DESCRIPTION, this));
        objectList.add(new TextEncodedStringSizeTerminated(DataTypes.OBJ_TEXT, this));
    }

}
