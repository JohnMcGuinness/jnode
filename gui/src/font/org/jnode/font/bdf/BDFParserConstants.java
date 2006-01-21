/* Generated By:JavaCC: Do not edit this line. BDFParserConstants.java */
/*
 Copyright (C) 2002-2006 Stephane Meslin-Weber <steph@tangency.co.uk>
 All rights reserved.
 
 This file is part of Odonata.
 
 Odonata is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 Odonata is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with GNU Classpath; see the file COPYING.  If not, write to
 the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 Boston, MA 02110-1301 USA.

 Linking this library statically or dynamically with other modules is
 making a combined work based on this library.  Thus, the terms and
 conditions of the GNU General Public License cover the whole
 combination.

 As a special exception, the copyright holders of this library give you
 permission to link this library with independent modules to produce an
 executable, regardless of the license terms of these independent
 modules, and to copy and distribute the resulting executable under
 terms of your choice, provided that you also meet, for each linked
 independent module, the terms and conditions of the license of that
 module.  An independent module is a module which is not derived from
 or based on this library.  If you modify this library, you may extend
 this exception to your version of the library, but you are not
 obligated to do so.  If you do not wish to do so, delete this
 exception statement from your version. 
*/
package uk.co.tangency.odonata.font.bdf.parser;

public interface BDFParserConstants {

  int EOF = 0;
  int SKIPPED = 2;
  int INT = 3;
  int STARTFONT = 4;
  int ENDFONT = 5;
  int CONTENTVERSION = 6;
  int FONTBOUNDINGBOX = 7;
  int METRICSSET = 8;
  int SIZE = 9;
  int CHARS = 10;
  int STARTCHAR = 11;
  int CHARTEXT = 13;
  int ENCODING = 15;
  int SWIDTH = 16;
  int DWIDTH = 17;
  int BBX = 18;
  int BITMAP = 19;
  int HEX = 20;
  int ENDCHAR = 21;
  int STARTPROPERTIES = 23;
  int PROPERTYCOUNT = 25;
  int ENDPROPERTIES = 26;
  int PROPERTYTEXT = 27;
  int COMMENT = 29;
  int CONTENTSTRING = 30;
  int FONTNAME = 31;
  int FONTFAMILYSTRING = 32;

  int DEFAULT = 0;
  int STARTCHARSTATE = 1;
  int CHARTEXTSTATE = 2;
  int CHARSTATE = 3;
  int BITMAPSTATE = 4;
  int PROPERTYCOUNTSTATE = 5;
  int PROPERTYSTATE = 6;
  int COMMENTSTATE = 7;
  int FONTSTATE = 8;

  String[] tokenImage = {
    "<EOF>",
    "\".\"",
    "<SKIPPED>",
    "<INT>",
    "\"STARTFONT\"",
    "\"ENDFONT\"",
    "\"CONTENTVERSION\"",
    "\"FONTBOUNDINGBOX\"",
    "\"METRICSSET\"",
    "\"SIZE\"",
    "\"CHARS\"",
    "\"STARTCHAR\"",
    "\" \"",
    "<CHARTEXT>",
    "<token of kind 14>",
    "\"ENCODING\"",
    "\"SWIDTH\"",
    "\"DWIDTH\"",
    "\"BBX\"",
    "\"BITMAP\"",
    "<HEX>",
    "\"ENDCHAR\"",
    "<token of kind 22>",
    "\"STARTPROPERTIES\"",
    "\" \"",
    "<PROPERTYCOUNT>",
    "\"ENDPROPERTIES\"",
    "<PROPERTYTEXT>",
    "<token of kind 28>",
    "\"COMMENT\"",
    "<CONTENTSTRING>",
    "\"FONT\"",
    "<FONTFAMILYSTRING>",
  };

}
