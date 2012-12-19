/**
 * Entities.
 * copyright (c) 2005-2009 Roedy Green, Canadian Mind Products
 * Version history
 *
 *  1.0 - initial version
 *
 * 1.1 - optimise using
 * text.indexOf('&amp;') and sb.append(string) rather
 * than processing    character by character.

 *  1.2 2004-07-21 - add stripHTMLTags -
 *                        stripFile also strips tags - add stripNbsp

 *  1.3 2005-06-20 - fix bug in possEntityToChar
 *                        - exposed possEntityToChar as public

 *  1.4 2005-07-02 - check for null input

 * 1.5 2005-07-29 - no longer needs entitiestochar.ser
 * file. Converted to JDK 1.5 back to 1,2
 *
 * 1.6 2005-09-05 - faster code for stripHTMLTags that
 * returns   original string if nothing changed.
 *
 * 1.8 2007-02-26 - fix bug. hex entity it &#xffff;  not &x#ffff;
 *
 * 2.2 2007-05-14 - StripHTMLTags now strips applet,
 * style, script pairs.
 *
 * 2.2 2007-05-14 generate hex entities as comments in entitycase.javafrag
 *
 * 2.3 2008-07-29 refactor code for Entities, add notes to chars, add a few new missing Entities.
 *
 * 2.4 2008-08-05 add translateNbspTo parameter to several methods and deprecate the versions without it.
 * This allows you to directly control how &nbsp; is translated, usually ' ' or (char)160.
 * Renamed methods to make it clearer just what sort of input is expect.
 *
 * 2.5 2008-08-06 add ability to insert XML entities.  Convert to JDK 1.5+, with generics, and for:each, StringBuilder
 */
package com.mindprod.entities;

import com.mindprod.common11.StringTools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Generates various text files describing entities. see main for details.
 *
 * @author Roedy Green version information in StripEntities
 * @version 2.5 2008-08-06 add ability to insert XML entities.  Convert to JDK 1.5+, with generics, and for:each, StringBuilder
 * @noinspection WeakerAccess
 */
public final class Entities
    {
    // ------------------------------ FIELDS ------------------------------

    /**
     * used to print 8 items per line.
     */
    private static int counter = 0;
    /**
     * entities as case statements to convert number to entity.
     */
    private static PrintWriter entityCase;

    /**
     * entities as case statements to use in StripQuotes to get rid of accented chars
     */
    private static PrintWriter entityCaseHex;

    /**
     * entities as associate statements to put code for this class into canonical form.
     */
    private static PrintWriter entityFacts;

    /**
     * entity names without &;.
     */
    private static PrintWriter entityJustKeys;

    /**
     * character codes.
     */
    private static PrintWriter entityJustValues;

    /**
     * entities for the HTML Cheat sheet.
     */
    private static PrintWriter entitySpecial;

    /**
     * For Vslick editor vslick\builtins\html.tagdoc.
     */
    private static PrintWriter entityVslickHtmlTagdoc;

    /**
     * For Vslick editor user.vlx.
     */
    private static PrintWriter entityVslickVlx;
    /**
     * undisplayed copyright notice
     *
     * @noinspection UnusedDeclaration
     */
    public static final String EMBEDDED_COPYRIGHT =
            "copyright (c) 2005-2009 Roedy Green, Canadian Mind Products, http://mindprod.com";

    /**
     * @noinspection UnusedDeclaration
     */
    private static final String RELEASE_DATE = "2008-08-06";

    /**
     * title of this package.
     *
     * @noinspection UnusedDeclaration
     */
    private static final String TITLE_STRING = "Entities";

    /**
     * version of this code.
     *
     * @noinspection UnusedDeclaration
     */
    private static final String VERSION_STRING = "2.5";

    // -------------------------- STATIC METHODS --------------------------

    /**
     * Used to build the entityToChar conversion table. This is not a constructor. It builds nothing and leaves nothing
     * behind.  It just emits various bits of source code for use elsewhere.
     *
     * @param theCharNumber Character equivalent
     * @param entity        entity with &..;
     * @param description   description of this character,  no html allowed. No quotes
     * @param notes         html giving extra info about this entity. No quotes
     * @param category      l=lower case Latin L=upper case Latin (alpha accents) g=lower case Greek G=upper case Greek
     *                      a=arrow s=symbol. n=not an entity.
     */
    private static void associate( String entity,
                                   int theCharNumber,
                                   String description,
                                   String notes,
                                   char category )
        {
        // Print out in number order, one line in each file each
        // time associate is called.
        associateEntityCase( entity, theCharNumber, description, category );
        associateEntityJustKeys( entity, theCharNumber, description, category );
        associateEntityCaseHex( entity, theCharNumber, category );
        associateEntityFacts( entity, theCharNumber, description, notes, category );
        associateEntityJustValues( entity, theCharNumber, description, category );
        associateEntitySpecial( entity, theCharNumber, description, notes, category );
        associateEntityVslickHtmlTagdoc( entity, theCharNumber, description, category );
        associateEntityVlsickVlx( entity, category );
        }

    /**
     * Display Java source code for the association of number to entity
     * For InsertEntities  as a case clause.
     *
     * @param entity        entity with &..;
     * @param theCharNumber unicode number of the character
     * @param description   short description of the char
     * @param category      l=lower case Latin L=upper case Latin (alpha accents) g=lower case Greek G=upper case Greek
     *                      a=arrow s=symbol. n=not an entity.
     */
    private static void associateEntityCase( String entity, int theCharNumber, String description, char category )
        {
        if ( category != 'n' )
            {
            StringBuilder sb = new StringBuilder( 400 );

            sb.append( "case " );
            sb.append( StringTools.leftPad( Integer.toString( theCharNumber ),
                    4,
                    false ) );
            sb.append( " : return " );
            sb.append( StringTools.rightPad( "\"" + entity + "\"",
                    StripEntities
                            .LONGEST_ENTITY + 2,
                    false ) );
            sb.append( " /* " );
            sb.append( "&#x" );
            sb.append( Integer.toHexString( theCharNumber ) );
            sb.append( "; " );
            sb.append( description );
            sb.append( " */;" );
            // Java case   34 : return "&quot;"     /* &#x22; quotation mark */;
            entityCase.println( sb.toString() );
            }
        }

    private static void associateEntityCaseHex( String entity, int theCharNumber, char category )
        {
        if ( category != 'n' )
            {
            StringBuilder sb = new StringBuilder( 400 );

            // Display Java source code Tools.stripQuotes to get rid of accented letters
            sb.append( "case " );
            sb.append( StringTools.rightPad( "/* " + entity + " */",
                    14,
                    false ) );
            sb.append( " \'\\u" );
            sb.append( StringTools.toLzHexString( theCharNumber, 4 ) );
            sb.append( "\' :" );
            // Java case /* AElig */    '\u00c6' :
            entityCaseHex.println( sb.toString() );
            }
        }

    private static void associateEntityFacts( String entity, int theCharNumber, String description, String notes, char category )
        {
        // Display Java source code for the association of number to entity
        // For this class
        {
        StringBuilder sb = new StringBuilder( 400 );

        sb.append( "associate( " );
        sb.append( StringTools.rightPad( "\"" + entity + "\"",
                StripEntities
                        .LONGEST_ENTITY + 2,
                false ) );
        sb.append( ", 0x" );
        sb.append( StringTools.toLzHexString( theCharNumber, 4 ) );
        sb.append( " " );
        sb.append( " /* " );
        sb.append( StringTools.leftPad( Integer.toString( theCharNumber ), 4, false ) );
        sb.append( " */, \"" );
        sb.append( description );
        sb.append( "\", \"" );
        sb.append( notes );
        sb.append( "\", '" );
        sb.append( category );
        sb.append( "\' );" );
        //  associate( "&shy;", 0x0173, "soft hyphen", "always visible", 's' );
        entityFacts.println( sb.toString() );
        }
        }

    private static void associateEntityJustKeys( String entity, int theCharNumber, String description, char category )
        {
        if ( category != 'n' )
            {
            // list of entities for array init with C-style comment what they are
            // for StripEntities
            StringBuilder sb = new StringBuilder( 400 );
            final String choppedEntity = entity.substring( 1, entity.length() - 1 );
            sb.append( StringTools.rightPad(
                    "\"" + choppedEntity + "\"",
                    StripEntities
                            .LONGEST_ENTITY,
                    false ) );
            sb.append( " /* " );
            sb.append( StringTools.leftPad( Integer.toString( theCharNumber ),
                    4,
                    false ) );
            sb.append( " : " );
            sb.append( "&#x" );
            sb.append( Integer.toHexString( theCharNumber ) );
            sb.append( "; " );
            sb.append( description );
            sb.append( " */," );
            // java: "quot"     /*   34 : &#x22; quotation mark */,
            entityJustKeys.println( sb.toString() );
            }
        }

    private static void associateEntityJustValues( String entity, int theCharNumber, String description, char category )
        {
        if ( category != 'n' )
            {
            StringBuilder sb = new StringBuilder( 400 );

            // list of entity numbers for array init with C-style comment what they are
            // for StripEntities
            sb.append( StringTools.leftPad(
                    Integer.toString( theCharNumber ),
                    4,
                    false ) );
            sb.append( " /* " );
            sb.append( StringTools.rightPad( entity, StripEntities
                    .LONGEST_ENTITY, false ) );
            sb.append( " : " );
            sb.append( "&#x" );
            sb.append( Integer.toHexString( theCharNumber ) );
            sb.append( "; " );
            sb.append( description );
            sb.append( " */," );
            // java 34 /* &quot;     : &#x22; quotation mark */
            entityJustValues.println( sb.toString() );
            }
        }

    private static void associateEntitySpecial( String entity, int theCharNumber, String description, String notes, char category )
        {
        if ( "agGns".indexOf( category ) >= 0 )
            {
            StringBuilder sb = new StringBuilder( 400 );

            // for HTML cheat sheet, after massaging.
            sb.append( "<tr>" );
            sb.append( "<td>" );
            sb.append( entity );
            sb.append( "</td>" );
            sb.append( "<td>" );
            if ( entity.startsWith( "&" ) )
                {
                final String choppedEntity = entity.substring( 1, entity.length() - 1 );
                sb.append( "&amp;" );
                sb.append( choppedEntity );
                sb.append( ";" );
                }
            else
                {
                sb.append( entity );// handle n non-entity
                }
            sb.append( "</td>" );
            sb.append( "<td>&amp;#x" );
            sb.append( Integer.toHexString( theCharNumber ) );
            sb.append( ";</td>" );
            sb.append( "<td>&amp;#" );
            sb.append( Integer.toString( theCharNumber ) );
            sb.append( ";</td>" );
            sb.append( "<td>" );
            sb.append( "\'\\u" );
            sb.append( StringTools.toLzHexString( theCharNumber, 4 ) );
            sb.append( '\'' );
            sb.append( "</td>" );
            sb.append( "<td>" );
            sb.append( description );
            sb.append( "</td>" );
            sb.append( "<td>" );
            sb.append( notes );
            sb.append( "</td>" );
            sb.append( "</tr>" );
            // html rendered, char entity, hex entity, java literal, desc
            // html: | &quot;  &amp;quot; | &amp;#x22; | '\u0022' | quotation mark
            entitySpecial.println( sb.toString() );
            }
        }

    private static void associateEntityVlsickVlx( String entity, char category )
        {
        if ( category != 'n' )
            {
            StringBuilder sb = new StringBuilder( 400 );
            //  8 entries per line
            if ( counter++ % 8 == 0 )
                {
                sb.append( "\ncskeywords=" );
                }

            // cskeywords= &AElig; &Aacute; &Acirc; &Agrave; &Aring; &Atilde; &Auml;
            sb.append( ' ' );
            sb.append( entity );
            entityVslickVlx.print( sb.toString() );
            }
        }

    private static void associateEntityVslickHtmlTagdoc( String entity, int theCharNumber, String description, char category )
        {
        if ( category != 'n' )
            {
            StringBuilder sb = new StringBuilder( 400 );

            // For SlickEdit list of entities.
            sb.append( "const " );
            final String choppedEntity = entity.substring( 1, entity.length() - 1 );
            sb.append( StringTools.rightPad( choppedEntity + ";", StripEntities
                    .LONGEST_ENTITY - 1, false ) );
            sb.append( " // " );
            sb.append( StringTools.leftPad( Integer.toString( theCharNumber ),
                    4,
                    false ) );
            sb.append( " : " );
            sb.append( "&#x" );
            sb.append( Integer.toHexString( theCharNumber ) );
            sb.append( "; " );
            sb.append( description );
            // const quot;     //   34 : &#x22; quotation mark
            entityVslickHtmlTagdoc.println( sb.toString() );
            }
        }

    private static void closeFiles()
        {
        // C L O S E
        entityCase.close();
        entityCaseHex.close();
        entityFacts.close();
        entityJustKeys.close();
        entitySpecial.close();
        entityJustValues.close();
        entityVslickHtmlTagdoc.close();
        entityVslickVlx.close();
        }

    private static void makeAssociations
            ()
        {
        // List generated from http://www.w3.org/TR/html4/sgml/entities.html
        // on 2005-06-20
        // revised 2008-08-29
        // insert various generated files where they belong.  see main for details.

        associate( "&quot;", 0x0022/*   34 */, "quotation mark", "double quote. APL quote. Usually use the &amp;ldquo; and &amp;rdquo;", 's' );
        associate( "&amp;", 0x0026/*   38 */, "ampersand", "", 's' );
        associate( "'", 0x0027/*   39 */, "apostrophe", "no entity needed, single quote, use &amp;grave; and acute for left/right, or &amp;lsquo; and &amp;rsquo;. &amp;apos; is not officially recognised.", 'n' );
        associate( "&lt;", 0x003c/*   60 */, "less-than sign", "", 's' );
        associate( "&gt;", 0x003e/*   62 */, "greater-than sign", "", 's' );
        associate( "@", 0x0040/*   64 */, "at sign", "no entity needed. &amp;at; is not officially recognised.", 'n' );
        associate( "^", 0x005e/*   94 */, "circumflex", "no entity needed. spacing ^, see also &amp;circ; &circ;", 'n' );
        associate( "`", 0x0060/*   96 */, "grave", "no entity needed.", 'n' );
        associate( "~", 0x007e/*  126 */, "tilde", "spacing tilde not a dead key. see &amp;sim &sim;", 'n' );
        associate( "&nbsp;", 0x00a0/*  160 */, "non-breaking space", "&rarr;&nbsp;&larr;", 's' );
        associate( "&iexcl;", 0x00a1/*  161 */, "inverted exclamation mark", "", 's' );
        associate( "&cent;", 0x00a2/*  162 */, "cent sign", "", 's' );
        associate( "&pound;", 0x00a3/*  163 */, "pound sign", "", 's' );
        associate( "&curren;", 0x00a4/*  164 */, "currency sign", "", 's' );
        associate( "&yen;", 0x00a5/*  165 */, "yen sign", "", 's' );
        associate( "&brvbar;", 0x00a6/*  166 */, "broken bar", "", 's' );
        associate( "&sect;", 0x00a7/*  167 */, "section sign", "like two Ss joins forming a circle in the middle", 's' );
        associate( "&uml;", 0x00a8/*  168 */, "diaeresis", "umlaut, spacing diaeresis, not a dead char", 's' );
        associate( "&copy;", 0x00a9/*  169 */, "copyright sign", "", 's' );
        associate( "&ordf;", 0x00aa/*  170 */, "feminine ordinal indicator", "", 's' );
        associate( "&laquo;", 0x00ab/*  171 */, "left guillemot", "left angled quote, left-pointing double angle quotation mark", 's' );
        associate( "&not;", 0x00ac/*  172 */, "not sign", "", 's' );
        associate( "&shy;", 0x00ad/*  173 */, "soft hyphen", "The soft hyphen is always visible, and may look slightly different from a normal hyphen. It is intended for use at ends of line to indicate a word break. There is no such thing as a non-breaking hyphen that forces text on both sides to stay on the same  there is no such thing as a discretionary hyphen that marks a good place to split a word over two lines if necessary, invisible unless the word breaks over two lines.", 's' );
        associate( "&reg;", 0x00ae/*  174 */, "registered sign", "registered trade mark sign. Illegal for a vendor to use without legal registration. See also &amp;trade; &trade;", 's' );
        associate( "&macr;", 0x00af/*  175 */, "macron", " spacing macron, not dead key, overline", 's' );
        associate( "&deg;", 0x00b0/*  176 */, "degree sign", "", 's' );
        associate( "&plusmn;", 0x00b1/*  177 */, "plus-minus sign", "", 's' );
        associate( "&sup2;", 0x00b2/*  178 */, "superscript two", "You can also use <sup>2</sup> &lt;sup&gt;2&lt;/sup&gt;", 's' );
        associate( "&sup3;", 0x00b3/*  179 */, "superscript three", "You can also use <sup>3</sup> &lt;sup&gt;3&lt;/sup&gt;", 's' );
        associate( "&acute;", 0x00b4/*  180 */, "acute accent", "spacing acute, not dead key", 's' );
        associate( "&micro;", 0x00b5/*  181 */, "micro sign", "like &amp;mu; &mu;", 's' );
        associate( "&para;", 0x00b6/*  182 */, "pilcrow sign", "paragraph sign, like S with circle", 's' );
        associate( "&middot;", 0x00b7/*  183 */, "middle dot", "", 's' );
        associate( "&cedil;", 0x00b8/*  184 */, "cedilla", "spacing cedilla, not dead key, looks like subscript c", 's' );
        associate( "&sup1;", 0x00b9/*  185 */, "superscript one", "You can also use <sup>1</sup> &lt;sup&gt;1&lt;/sup&gt;", 's' );
        associate( "&ordm;", 0x00ba/*  186 */, "masculine ordinal indicator", "", 's' );
        associate( "&raquo;", 0x00bb/*  187 */, "right guillemot", "right-pointing double angle quotation mark", 's' );
        associate( "&frac14;", 0x00bc/*  188 */, "vulgar fraction 1/4", "", 's' );
        associate( "&frac12;", 0x00bd/*  189 */, "vulgar fraction 1/2", "", 's' );
        associate( "&frac34;", 0x00be/*  190 */, "vulgar fraction 3/4", "", 's' );
        associate( "&iquest;", 0x00bf/*  191 */, "inverted question mark", "", 's' );
        associate( "&Agrave;", 0x00c0/*  192 */, "Latin capital letter A with grave", "", 'L' );
        associate( "&Aacute;", 0x00c1/*  193 */, "Latin capital letter A with acute", "", 'L' );
        associate( "&Acirc;", 0x00c2/*  194 */, "Latin capital letter A with circumflex", "", 'L' );
        associate( "&Atilde;", 0x00c3/*  195 */, "Latin capital letter A with tilde", "", 'L' );
        associate( "&Auml;", 0x00c4/*  196 */, "Latin capital letter A with diaeresis", "", 'L' );
        associate( "&Aring;", 0x00c5/*  197 */, "Latin capital letter A with ring above", "", 'L' );
        associate( "&AElig;", 0x00c6/*  198 */, "Latin capital letter AE", "", 'L' );
        associate( "&Ccedil;", 0x00c7/*  199 */, "Latin capital letter C with cedilla", "", 'L' );
        associate( "&Egrave;", 0x00c8/*  200 */, "Latin capital letter E with grave", "", 'L' );
        associate( "&Eacute;", 0x00c9/*  201 */, "Latin capital letter E with acute", "", 'L' );
        associate( "&Ecirc;", 0x00ca/*  202 */, "Latin capital letter E with circumflex", "", 'L' );
        associate( "&Euml;", 0x00cb/*  203 */, "Latin capital letter E with diaeresis", "", 'L' );
        associate( "&Igrave;", 0x00cc/*  204 */, "Latin capital letter I with grave", "", 'L' );
        associate( "&Iacute;", 0x00cd/*  205 */, "Latin capital letter I with acute", "", 'L' );
        associate( "&Icirc;", 0x00ce/*  206 */, "Latin capital letter I with circumflex", "", 'L' );
        associate( "&Iuml;", 0x00cf/*  207 */, "Latin capital letter I with diaeresis", "", 'L' );
        associate( "&ETH;", 0x00d0/*  208 */, "Latin capital letter eth", "", 'L' );
        associate( "&Ntilde;", 0x00d1/*  209 */, "Latin capital letter N with tilde", "", 'L' );
        associate( "&Ograve;", 0x00d2/*  210 */, "Latin capital letter O with grave", "", 'L' );
        associate( "&Oacute;", 0x00d3/*  211 */, "Latin capital letter O with acute", "", 'L' );
        associate( "&Ocirc;", 0x00d4/*  212 */, "Latin capital letter O with circumflex", "", 'L' );
        associate( "&Otilde;", 0x00d5/*  213 */, "Latin capital letter O with tilde", "", 'L' );
        associate( "&Ouml;", 0x00d6/*  214 */, "Latin capital letter O with diaeresis", "", 'L' );
        associate( "&times;", 0x00d7/*  215 */, "multiplication sign", "", 's' );
        associate( "&Oslash;", 0x00d8/*  216 */, "Latin capital letter O with stroke", "", 'L' );
        associate( "&Ugrave;", 0x00d9/*  217 */, "Latin capital letter U with grave", "", 'L' );
        associate( "&Uacute;", 0x00da/*  218 */, "Latin capital letter U with acute", "", 'L' );
        associate( "&Ucirc;", 0x00db/*  219 */, "Latin capital letter U with circumflex", "", 'L' );
        associate( "&Uuml;", 0x00dc/*  220 */, "Latin capital letter U with diaeresis", "", 'L' );
        associate( "&Yacute;", 0x00dd/*  221 */, "Latin capital letter Y with acute", "", 'L' );
        associate( "&THORN;", 0x00de/*  222 */, "Latin capital letter Thorn", "", 'l' );
        associate( "&szlig;", 0x00df/*  223 */, "Latin small letter sharp s", "German double s, looks like Beta", 'l' );
        associate( "&agrave;", 0x00e0/*  224 */, "Latin small letter a with grave", "", 'l' );
        associate( "&aacute;", 0x00e1/*  225 */, "Latin small letter a with acute", "", 'l' );
        associate( "&acirc;", 0x00e2/*  226 */, "Latin small letter a with circumflex", "", 'l' );
        associate( "&atilde;", 0x00e3/*  227 */, "Latin small letter a with tilde", "", 'l' );
        associate( "&auml;", 0x00e4/*  228 */, "Latin small letter a with diaeresis", "", 'l' );
        associate( "&aring;", 0x00e5/*  229 */, "Latin small letter a with ring above", "", 'l' );
        associate( "&aelig;", 0x00e6/*  230 */, "Latin lowercase ligature ae", "", 'l' );
        associate( "&ccedil;", 0x00e7/*  231 */, "Latin small letter c with cedilla", "", 'l' );
        associate( "&egrave;", 0x00e8/*  232 */, "Latin small letter e with grave", "", 'l' );
        associate( "&eacute;", 0x00e9/*  233 */, "Latin small letter e with acute", "", 'l' );
        associate( "&ecirc;", 0x00ea/*  234 */, "Latin small letter e with circumflex", "", 'l' );
        associate( "&euml;", 0x00eb/*  235 */, "Latin small letter e with diaeresis", "", 'l' );
        associate( "&igrave;", 0x00ec/*  236 */, "Latin small letter i with grave", "", 'l' );
        associate( "&iacute;", 0x00ed/*  237 */, "Latin small letter i with acute", "", 'l' );
        associate( "&icirc;", 0x00ee/*  238 */, "Latin small letter i with circumflex", "", 'l' );
        associate( "&iuml;", 0x00ef/*  239 */, "Latin small letter i with diaeresis", "", 'l' );
        associate( "&eth;", 0x00f0/*  240 */, "Latin small letter eth", "", 'l' );
        associate( "&ntilde;", 0x00f1/*  241 */, "Latin small letter n with tilde", "", 'l' );
        associate( "&ograve;", 0x00f2/*  242 */, "Latin small letter o with grave", "", 'l' );
        associate( "&oacute;", 0x00f3/*  243 */, "Latin small letter o with acute", "", 'l' );
        associate( "&ocirc;", 0x00f4/*  244 */, "Latin small letter o with circumflex", "", 'l' );
        associate( "&otilde;", 0x00f5/*  245 */, "Latin small letter o with tilde", "", 'l' );
        associate( "&ouml;", 0x00f6/*  246 */, "Latin small letter o with diaeresis", "", 'l' );
        associate( "&divide;", 0x00f7/*  247 */, "division sign", "", 's' );
        associate( "&oslash;", 0x00f8/*  248 */, "Latin small letter o with stroke", "", 'l' );
        associate( "&ugrave;", 0x00f9/*  249 */, "Latin small letter u with grave", "", 'l' );
        associate( "&uacute;", 0x00fa/*  250 */, "Latin small letter u with acute", "", 'l' );
        associate( "&ucirc;", 0x00fb/*  251 */, "Latin small letter u with circumflex", "", 'l' );
        associate( "&uuml;", 0x00fc/*  252 */, "Latin small letter u with diaeresis", "", 'l' );
        associate( "&yacute;", 0x00fd/*  253 */, "Latin small letter y with acute", "", 'l' );
        associate( "&thorn;", 0x00fe/*  254 */, "Latin small letter thorn", "", 'l' );
        associate( "&yuml;", 0x00ff/*  255 */, "Latin small letter y with diaeresis", "", 'l' );
        associate( "&OElig;", 0x0152/*  338 */, "Latin capital ligature oe", "", 'l' );
        associate( "&oelig;", 0x0153/*  339 */, "Latin small ligature oe", "", 'l' );
        associate( "&Scaron;", 0x0160/*  352 */, "Latin capital letter S with caron", "", 'l' );
        associate( "&scaron;", 0x0161/*  353 */, "Latin small letter s with caron", "", 'l' );
        associate( "&Yuml;", 0x0178/*  376 */, "Latin capital letter Y with diaeresis", "", 'l' );
        associate( "&fnof;", 0x0192/*  402 */, "Latin small letter f with hook", "", 'l' );
        associate( "&circ;", 0x02c6/*  710 */, "modifier letter circumflex accent", "acts like a ^ dead key", 's' );
        associate( "&tilde;", 0x02dc/*  732 */, "small tilde", "see also ~ and &amp;sim; &sim;", 's' );
        associate( "&Alpha;", 0x0391/*  913 */, "Greek capital letter Alpha", "", 'G' );
        associate( "&Beta;", 0x0392/*  914 */, "Greek capital letter Beta", "", 'G' );
        associate( "&Gamma;", 0x0393/*  915 */, "Greek capital letter Gamma", "", 'G' );
        associate( "&Delta;", 0x0394/*  916 */, "Greek capital letter Delta", "", 'G' );
        associate( "&Epsilon;", 0x0395/*  917 */, "Greek capital letter Epsilon", "", 'G' );
        associate( "&Zeta;", 0x0396/*  918 */, "Greek capital letter Zeta", "", 'G' );
        associate( "&Eta;", 0x0397/*  919 */, "Greek capital letter Eta", "", 'G' );
        associate( "&Theta;", 0x0398/*  920 */, "Greek capital letter Theta", "", 'G' );
        associate( "&Iota;", 0x0399/*  921 */, "Greek capital letter Iota", "", 'G' );
        associate( "&Kappa;", 0x039a/*  922 */, "Greek capital letter Kappa", "", 'G' );
        associate( "&Lambda;", 0x039b/*  923 */, "Greek capital letter Lambda", "", 'G' );
        associate( "&Mu;", 0x039c/*  924 */, "Greek capital letter Mu", "", 'G' );
        associate( "&Nu;", 0x039d/*  925 */, "Greek capital letter Nu", "", 'G' );
        associate( "&Xi;", 0x039e/*  926 */, "Greek capital letter Xi", "", 'G' );
        associate( "&Omicron;", 0x039f/*  927 */, "Greek capital letter Omicron", "", 'G' );
        associate( "&Pi;", 0x03a0/*  928 */, "Greek capital letter Pi", "like &amp;prod; &prod;", 'G' );
        associate( "&Rho;", 0x03a1/*  929 */, "Greek capital letter Rho", "", 'G' );
        associate( "&Sigma;", 0x03a3/*  931 */, "Greek capital letter Sigma", "like &amp;sum; &sum;", 'G' );
        associate( "&Tau;", 0x03a4/*  932 */, "Greek capital letter Tau", "", 'G' );
        associate( "&Upsilon;", 0x03a5/*  933 */, "Greek capital letter Upsilon", "", 'G' );
        associate( "&Phi;", 0x03a6/*  934 */, "Greek capital letter Phi", "", 'G' );
        associate( "&Chi;", 0x03a7/*  935 */, "Greek capital letter Chi", "", 'G' );
        associate( "&Psi;", 0x03a8/*  936 */, "Greek capital letter Psi", "", 'G' );
        associate( "&Omega;", 0x03a9/*  937 */, "Greek capital letter Omega", "", 'G' );
        associate( "&alpha;", 0x03b1/*  945 */, "Greek small letter alpha", "", 'G' );
        associate( "&beta;", 0x03b2/*  946 */, "Greek small letter beta", "", 'G' );
        associate( "&gamma;", 0x03b3/*  947 */, "Greek small letter gamma", "", 'G' );
        associate( "&delta;", 0x03b4/*  948 */, "Greek small letter delta", "", 'G' );
        associate( "&epsilon;", 0x03b5/*  949 */, "Greek small letter epsilon", "", 'G' );
        associate( "&zeta;", 0x03b6/*  950 */, "Greek small letter zeta", "", 'G' );
        associate( "&eta;", 0x03b7/*  951 */, "Greek small letter eta", "", 'G' );
        associate( "&theta;", 0x03b8/*  952 */, "Greek small letter theta", "", 'G' );
        associate( "&iota;", 0x03b9/*  953 */, "Greek small letter iota", "", 'G' );
        associate( "&kappa;", 0x03ba/*  954 */, "Greek small letter kappa", "", 'G' );
        associate( "&lambda;", 0x03bb/*  955 */, "Greek small letter lambda", "", 'G' );
        associate( "&mu;", 0x03bc/*  956 */, "Greek small letter mu", "like &amp;micro; &micro;", 'g' );
        associate( "&nu;", 0x03bd/*  957 */, "Greek small letter nu", "", 'g' );
        associate( "&xi;", 0x03be/*  958 */, "Greek small letter xi", "", 'g' );
        associate( "&omicron;", 0x03bf/*  959 */, "Greek small letter omicron", "", 'g' );
        associate( "&pi;", 0x03c0/*  960 */, "Greek small letter pi", "", 'g' );
        associate( "&rho;", 0x03c1/*  961 */, "Greek small letter rho", "", 'g' );
        associate( "&sigmaf;", 0x03c2/*  962 */, "Greek small letter final sigma", "", 'g' );
        associate( "&sigma;", 0x03c3/*  963 */, "Greek small letter sigma", "", 'g' );
        associate( "&tau;", 0x03c4/*  964 */, "Greek small letter tau", "", 'g' );
        associate( "&upsilon;", 0x03c5/*  965 */, "Greek small letter upsilon", "", 'g' );
        associate( "&phi;", 0x03c6/*  966 */, "Greek small letter phi", "", 'g' );
        associate( "&chi;", 0x03c7/*  967 */, "Greek small letter chi", "", 'g' );
        associate( "&psi;", 0x03c8/*  968 */, "Greek small letter psi", "", 'g' );
        associate( "&omega;", 0x03c9/*  969 */, "Greek small letter omega", "", 'g' );
        associate( "&thetasym;", 0x03d1/*  977 */, "Greek theta symbol", "", 'g' );
        associate( "&upsih;", 0x03d2/*  978 */, "Greek upsilon with hook symbol", "", 'g' );
        associate( "&piv;", 0x03d6/*  982 */, "Greek pi symbol", "not ordinary pi, looks like omega bar,", 'g' );
        associate( "&ensp;", 0x2002/* 8194 */, "en space", "&rarr;&ensp;&larr;", 's' );
        associate( "&emsp;", 0x2003/* 8195 */, "em space", "&rarr;&emsp;&larr;", 's' );
        associate( "&thinsp;", 0x2009/* 8201 */, "thin space", "&rarr;&thinsp;&larr;", 's' );
        associate( "&zwnj;", 0x200c/* 8204 */, "zero width non-joiner", "", 's' );
        associate( "&zwj;", 0x200d/* 8205 */, "zero width joiner", "", 's' );
        associate( "&lrm;", 0x200e/* 8206 */, "left-to-right mark", "", 's' );
        associate( "&rlm;", 0x200f/* 8207 */, "right-to-left mark", "", 's' );
        associate( "&ndash;", 0x2013/* 8211 */, "en dash", "compare &mdash; &amp;mdash; &minus; &amp;minus; &ndash; &amp;ndash; - ordinary dash", 's' );
        associate( "&mdash;", 0x2014/* 8212 */, "em dash", "compare &mdash; &amp;mdash;, &minus; &amp;minus; &ndash; &amp;ndash; -  ordinary dash", 's' );
        associate( "&lsquo;", 0x2018/* 8216 */, "left single-6 quotation mark", "", 's' );
        associate( "&rsquo;", 0x2019/* 8217 */, "right single-9 quotation mark", "", 's' );
        associate( "&sbquo;", 0x201a/* 8218 */, "single low-9 quotation mark", "", 's' );
        associate( "&ldquo;", 0x201c/* 8220 */, "left double-66 quotation mark", "", 's' );
        associate( "&rdquo;", 0x201d/* 8221 */, "right double-99 quotation mark", "", 's' );
        associate( "&bdquo;", 0x201e/* 8222 */, "double low-99 quotation mark", "", 's' );
        associate( "&dagger;", 0x2020/* 8224 */, "dagger", "not widely supported. &amp;#134; only works in Latin-1", 's' );
        associate( "&Dagger;", 0x2021/* 8225 */, "double dagger", "", 's' );
        associate( "&bull;", 0x2022/* 8226 */, "bullet", "black small circle", 's' );
        associate( "&hellip;", 0x2026/* 8230 */, "horizontal ellipsis", "three dots in a row", 's' );
        associate( "&permil;", 0x2030/* 8240 */, "per mille sign", "", 's' );
        associate( "&prime;", 0x2032/* 8242 */, "prime", "indicates minutes or feet", 's' );
        associate( "&Prime;", 0x2033/* 8243 */, "double prime", "indicates seconds or inches", 's' );
        associate( "&lsaquo;", 0x2039/* 8249 */, "single left-pointing angle quotation mark", "", 's' );
        associate( "&rsaquo;", 0x203a/* 8250 */, "single right-pointing angle quotation mark", "", 's' );
        associate( "&oline;", 0x203e/* 8254 */, "overline", "spacing overline, not dead key", 's' );
        associate( "&frasl;", 0x2044/* 8260 */, "fraction slash", "", 's' );
        associate( "&euro;", 0x20ac/* 8364 */, "Euro currency sign", "&amp;#128; only works in Latin-1", 's' );
        associate( "&image;", 0x2111/* 8465 */, "black-letter capital i", "imaginary part", 's' );
        associate( "&weierp;", 0x2118/* 8472 */, "script capital p", "power set, Wierstrasse p", 's' );
        associate( "&real;", 0x211c/* 8476 */, "black-letter capital r", "real part symbol", 's' );
        associate( "&trade;", 0x2122/* 8482 */, "trademark sign", "unregistered Trademark. &amp;trade; only recently supported. Use &lt;sup&gt;TM&lt;/sup&gt; for safety. &amp;#153; only works in Latin-1.", 's' );
        associate( "&alefsym;", 0x2135/* 8501 */, "alef symbol", "first transfinite cardinal, note spelling alefysm not alephsym.", 's' );
        associate( "&larr;", 0x2190/* 8592 */, "leftwards arrow", "", 'a' );
        associate( "&uarr;", 0x2191/* 8593 */, "upwards arrow", "", 'a' );
        associate( "&rarr;", 0x2192/* 8594 */, "rightwards arrow", "", 'a' );
        associate( "&darr;", 0x2193/* 8595 */, "downwards arrow", "", 'a' );
        associate( "&harr;", 0x2194/* 8596 */, "left right arrow", "", 'a' );
        associate( "&crarr;", 0x21b5/* 8629 */, "downwards arrow with corner leftwards", "", 'a' );
        associate( "&lArr;", 0x21d0/* 8656 */, "leftwards double arrow", "", 'a' );
        associate( "&uArr;", 0x21d1/* 8657 */, "upwards double arrow", "", 'a' );
        associate( "&rArr;", 0x21d2/* 8658 */, "rightwards double arrow", "", 'a' );
        associate( "&dArr;", 0x21d3/* 8659 */, "downwards double arrow", "", 'a' );
        associate( "&hArr;", 0x21d4/* 8660 */, "left right double arrow", "", 'a' );
        associate( "&forall;", 0x2200/* 8704 */, "for all", "", 's' );
        associate( "&part;", 0x2202/* 8706 */, "partial differential", "", 's' );
        associate( "&exist;", 0x2203/* 8707 */, "there exists", "", 's' );
        associate( "&empty;", 0x2205/* 8709 */, "empty set", "null set, diameter", 's' );
        associate( "&nabla;", 0x2207/* 8711 */, "nabla", "backward difference. See &amp;Delta; &Delta;", 's' );
        associate( "&isin;", 0x2208/* 8712 */, "element of", "", 's' );
        associate( "&notin;", 0x2209/* 8713 */, "not an element of", "", 's' );
        associate( "&ni;", 0x220b/* 8715 */, "contains as member", "", 's' );
        associate( "&prod;", 0x220f/* 8719 */, "n-ary product", "product sign, like like &amp;Pi; &Pi;", 's' );
        associate( "&sum;", 0x2211/* 8721 */, "n-ary summation", "like &amp;Sigma; &Sigma;", 's' );
        associate( "&minus;", 0x2212/* 8722 */, "minus sign", "compare &mdash; &amp;mdash; &minus; &amp;minus; &ndash; &amp;ndash; - ordinary dash", 's' );
        associate( "&lowast;", 0x2217/* 8727 */, "asterisk operator", "", 's' );
        associate( "&radic;", 0x221a/* 8730 */, "square root", "radical sign", 's' );
        associate( "&prop;", 0x221d/* 8733 */, "proportional to", "", 's' );
        associate( "&infin;", 0x221e/* 8734 */, "infinity", "", 's' );
        associate( "&ang;", 0x2220/* 8736 */, "angle", "", 's' );
        associate( "&and;", 0x2227/* 8743 */, "logical and", "upside down v wedge", 's' );
        associate( "&or;", 0x2228/* 8744 */, "logical or", "vee", 's' );
        associate( "&cap;", 0x2229/* 8745 */, "intersection", "set theory, upside down U cap shape", 's' );
        associate( "&cup;", 0x222a/* 8746 */, "union", "set theory, U cup shape", 's' );
        associate( "&int;", 0x222b/* 8747 */, "integral", "", 's' );
        associate( "&there4;", 0x2234/* 8756 */, "therefore", "", 's' );
        associate( "&sim;", 0x223c/* 8764 */, "tilde operator", "varies with, similar to, see  ~ and &amp;tilde; &tilde;", 's' );
        associate( "&cong;", 0x2245/* 8773 */, "congruent to", "", 's' );
        associate( "&asymp;", 0x2248/* 8776 */, "asymptotic to", "almost equal to", 's' );
        associate( "&ne;", 0x2260/* 8800 */, "not equal to", "", 's' );
        associate( "&equiv;", 0x2261/* 8801 */, "identical to", "equivalent to", 's' );
        associate( "&le;", 0x2264/* 8804 */, "less-than or equal to", "", 's' );
        associate( "&ge;", 0x2265/* 8805 */, "greater-than or equal to", "", 's' );
        associate( "&sub;", 0x2282/* 8834 */, "subset of", "set theory, does not mean subcript.", 's' );
        associate( "&sup;", 0x2283/* 8835 */, "superset of", "set theory, does not mean superscript.", 's' );
        associate( "&nsub;", 0x2284/* 8836 */, "not a subset of", "set theory", 's' );
        associate( "&sube;", 0x2286/* 8838 */, "subset of or equal to", "set theory", 's' );
        associate( "&supe;", 0x2287/* 8839 */, "superset of or equal to", "set theory", 's' );
        associate( "&oplus;", 0x2295/* 8853 */, "circled plus", "direct sum", 's' );
        associate( "&otimes;", 0x2297/* 8855 */, "circled times", "vector product", 's' );
        associate( "&perp;", 0x22a5/* 8869 */, "up tack", "orthogonal to, perpendicular", 's' );
        associate( "&sdot;", 0x22c5/* 8901 */, "dot operator", "", 's' );
        associate( "&lceil;", 0x2308/* 8968 */, "left ceiling", "apl upstile", 's' );
        associate( "&rceil;", 0x2309/* 8969 */, "right ceiling", "apl downstile", 's' );
        associate( "&lfloor;", 0x230a/* 8970 */, "left floor", "", 's' );
        associate( "&rfloor;", 0x230b/* 8971 */, "right floor", "", 's' );
        associate( "&lang;", 0x2329/* 9001 */, "left-pointing angle bracket", "", 's' );
        associate( "&rang;", 0x232a/* 9002 */, "right-pointing angle bracket", "key", 's' );
        associate( "&loz;", 0x25ca/* 9674 */, "lozenge", "", 's' );
        associate( "&spades;", 0x2660/* 9824 */, "black spade suit", "", 's' );
        associate( "&clubs;", 0x2663/* 9827 */, "black club suit", "shamrock", 's' );
        associate( "&hearts;", 0x2665/* 9829 */, "black heart suit", "valentine", 's' );
        associate( "&diams;", 0x2666/* 9830 */, "black diamond suit", "", 's' );
        }

    private static void openFiles()
            throws IOException
        {
        // O P E N
        final boolean autoflush = true;

        entityCase = new PrintWriter( new BufferedWriter( new FileWriter( "entitycase.javafrag" ), 4096/* buffsize */ ), autoflush );
        entityCaseHex = new PrintWriter( new BufferedWriter( new FileWriter( "entitycasehex.javafrag" ), 4096/* buffsize */ ), autoflush );
        entityFacts = new PrintWriter( new BufferedWriter( new BufferedWriter( new FileWriter( "entityfacts.javafrag" ), 4096/* buffsize */ ), 4096/* buffsize */ ), autoflush );
        entityJustKeys = new PrintWriter( new BufferedWriter( new FileWriter( "entityjustkeys.javafrag" ), 4096/* buffsize */ ), autoflush );
        entitySpecial = new PrintWriter( new BufferedWriter( new FileWriter( "entityspecial.htmlfrag" ), 4096/* buffsize */ ), autoflush );
        entityJustValues = new PrintWriter( new BufferedWriter( new FileWriter( "entityjustvalues.javafrag" ), 4096/* buffsize */ ), autoflush );
        entityVslickHtmlTagdoc = new PrintWriter( new FileWriter( "entityvslickhtml.tagdoc" ), autoflush );
        entityVslickVlx = new PrintWriter( new FileWriter( "entityvslick.vlx" ), autoflush );
        }

    // --------------------------- main() method ---------------------------

    /**
     * Run once to generate [various text files that are inserted to handle entity coding.
     * generates the following files in current directory.
     * <p/>
     * entitycase.javafrag : case statements. Insert in InsertEntities.java, then recompile.
     * entitycasehex.javafrag : alternate case statements to Insert in InsertEntities.java, then recompile.
     * entityfacts.javafrag : insert into Entities.java and recompile. Tidies code.
     * entityjustkeys.javafrag  :  legal entity keys. Insert in StripEntities.java, then recompile.
     * entityjustvalues.javafrag  : legal entity values. Insert in StripEntities.java, then recompile.
     * entityspecial.htmlfrag : copy to E:\mindprod\jgloss\include\entityspecial.html (note extension change)
     * entityvslick.tagdoc : insert into F:\program files\vslick\builtins\html.tagdoc, just ones not there yet.
     * entityvslick.vlx  : Insert into F:\program files\vslick\vslick.vlx,
     * patch keywords= &amp; &lt; &gt; &quot;  instead of cskeywords.
     * <p/>
     *
     * @param args not used.
     * @throws java.io.IOException to get maximal info about the problem.
     */
    public static void main
            ( String[] args ) throws IOException
        {
        openFiles();
        // W R I T E , associate does the actual writing
        makeAssociations();
        closeFiles();
        }// end main
    }

// end Entities
