$( $t

/* The '$ t' (no space between '$' and 't') token indicates the beginning
    of the typesetting definition section, embedded in a Metamath
    comment.  There may only be one per source file, and the typesetting
    section ends with the end of the Metamath comment.  The typesetting
    section uses C-style comment delimiters.  Todo:  Allow multiple
    typesetting comments */

/* These are the LaTeX and HTML definitions in the order the tokens are
    introduced in $c or $v statements.  See HELP TEX or HELP HTML in the
    Metamath program. */


/******* Web page format settings *******/

/* Custom CSS for Unicode fonts */
/* The woff font file was obtained from
   http://fred-wang.github.io/MathFonts/XITS/xits-math.woff 28-Aug-2015 */
htmlcss '<STYLE TYPE="text/css">\n' +
    '<!--\n' +
    '  .setvar { color: red; }\n' +
    '  .wff { color: blue; }\n' +
    '  .class { color: #C3C; }\n' +
    '  .symvar { border-bottom:1px dotted;color:#C3C}\n' +
    '  .typecode { color: gray }\n' +
    '  .hidden { color: gray }\n' +
    '  @font-face {\n' +
    '    font-family: XITSMath-Regular;\n' +
    '    src: url(xits-math.woff);\n' +
    '  }\n' +
    '  .math { font-family: XITSMath-Regular }\n' +
    '-->\n' +
    '</STYLE>\n' +
    '<LINK href="mmset.css" title="mmset"\n' +
    '    rel="stylesheet" type="text/css">\n' +
    '<LINK href="mmsetalt.css" title="mmsetalt"\n' +
    '    rel="alternate stylesheet" type="text/css">';

/* Tag(s) for the main SPAN surrounding all Unicode math */
htmlfont 'CLASS=math';

/* Page title, home page link */
htmltitle "Intuitionistic Logic Explorer";
htmlhome '<A HREF="mmil.html"><FONT SIZE=-2 FACE=sans-serif>' +
    '<IMG SRC="_icon-il.gif" BORDER=0 ALT=' +
    '"Home" HEIGHT=32 WIDTH=32 ALIGN=MIDDLE>' +
    'Home</FONT></A>';
/* Optional file where bibliographic references are kept */
/* If specified, e.g. "mmset.html", Metamath will hyperlink all strings of the
   form "[rrr]" (where "rrr" has no whitespace) to "mmset.html#rrr" */
/* A warning will be given if the file "mmset.html" with the bibliographical
   references is not present.  It is read in order to check correctness of
   the references. */
htmlbibliography "mmil.html";

/* Variable color key at the bottom of each proof table */
htmlvarcolor '<FONT COLOR="#0000FF">wff</FONT> '
    + '<FONT COLOR="#FF0000">set</FONT> '
    + '<FONT COLOR="#CC33CC">class</FONT>';

/* GIF and Unicode HTML directories - these are used for the GIF version to
   crosslink to the Unicode version and vice-versa */
htmldir "../ilegif/";
althtmldir "../ileuni/";


/******* Symbol definitions *******/

htmldef "(" as "<IMG SRC='lp.gif' WIDTH=5 HEIGHT=19 TITLE='(' ALIGN=TOP>";
  althtmldef "(" as "(";
  latexdef "(" as "(";
htmldef ")" as "<IMG SRC='rp.gif' WIDTH=5 HEIGHT=19 TITLE=')' ALIGN=TOP>";
  althtmldef ")" as ")";
  latexdef ")" as ")";
htmldef "->" as
    " <IMG SRC='to.gif' WIDTH=15 HEIGHT=19 TITLE='-&gt;' ALIGN=TOP> ";
  althtmldef "->" as ' &rarr; ';
  latexdef "->" as "\rightarrow";
htmldef "-." as
    "<IMG SRC='lnot.gif' WIDTH=10 HEIGHT=19 TITLE='-.' ALIGN=TOP> ";
  althtmldef "-." as '&not; ';
  latexdef "-." as "\lnot";
htmldef "wff" as
    "<IMG SRC='_wff.gif' WIDTH=24 HEIGHT=19 TITLE='wff' ALIGN=TOP> ";
  althtmldef "wff" as '<FONT COLOR="#808080">wff </FONT>'; /* was #00CC00 */
  latexdef "wff" as "{\rm wff}";
htmldef "|-" as
    "<IMG SRC='_vdash.gif' WIDTH=10 HEIGHT=19 TITLE='|-' ALIGN=TOP> ";
  althtmldef "|-" as
    '<FONT COLOR="#808080" FACE=sans-serif>&#8866; </FONT>'; /* &vdash; */
    /* Without sans-serif, way too big in FF3 */
  latexdef "|-" as "\vdash";
htmldef "&" as
    " <IMG SRC='amp.gif' WIDTH=12 HEIGHT=19 ALT='&amp;'> ";
  althtmldef "&" as " &amp; ";
  latexdef "&" as " & ";
htmldef "=>" as
  " <IMG SRC='bigto.gif' WIDTH=15 HEIGHT=19 ALT='=&gt;'> ";
  althtmldef "=>" as " &rArr; ";
  latexdef "=>" as " \Rightarrow ";
htmldef "ph" as
    "<IMG SRC='_varphi.gif' WIDTH=11 HEIGHT=19 TITLE='ph' ALIGN=TOP>";
  althtmldef "ph" as '<FONT COLOR="#0000FF"><I>&phi;</I></FONT>';
  latexdef "ph" as "\varphi";
htmldef "ps" as "<IMG SRC='_psi.gif' WIDTH=12 HEIGHT=19 TITLE='ps' ALIGN=TOP>";
  althtmldef "ps" as '<FONT COLOR="#0000FF"><I>&psi;</I></FONT>';
  latexdef "ps" as "\psi";
htmldef "ch" as "<IMG SRC='_chi.gif' WIDTH=12 HEIGHT=19 TITLE='ch' ALIGN=TOP>";
  althtmldef "ch" as '<FONT COLOR="#0000FF"><I>&chi;</I></FONT>';
  latexdef "ch" as "\chi";
htmldef "th" as
    "<IMG SRC='_theta.gif' WIDTH=8 HEIGHT=19 TITLE='th' ALIGN=TOP>";
  althtmldef "th" as '<FONT COLOR="#0000FF"><I>&theta;</I></FONT>';
  latexdef "th" as "\theta";
htmldef "ta" as "<IMG SRC='_tau.gif' WIDTH=10 HEIGHT=19 TITLE='ta' ALIGN=TOP>";
  althtmldef "ta" as '<FONT COLOR="#0000FF"><I>&tau;</I></FONT>';
  latexdef "ta" as "\tau";
htmldef "et" as "<IMG SRC='_eta.gif' WIDTH=9 HEIGHT=19 TITLE='et' ALIGN=TOP>";
  althtmldef "et" as '<FONT COLOR="#0000FF"><I>&eta;</I></FONT>';
  latexdef "et" as "\eta";
htmldef "ze" as "<IMG SRC='_zeta.gif' WIDTH=9 HEIGHT=19 TITLE='ze' ALIGN=TOP>";
  althtmldef "ze" as '<FONT COLOR="#0000FF"><I>&zeta;</I></FONT>';
  latexdef "ze" as "\zeta";
htmldef "si" as
    "<IMG SRC='_sigma.gif' WIDTH=10 HEIGHT=19 TITLE='si' ALIGN=TOP>";
  althtmldef "si" as '<FONT COLOR="#0000FF"><I>&sigma;</I></FONT>';
  latexdef "si" as "\sigma";
htmldef "rh" as "<IMG SRC='_rho.gif' WIDTH=9 HEIGHT=19 TITLE='rh' ALIGN=TOP>";
  althtmldef "rh" as '<FONT COLOR="#0000FF"><I>&rho;</I></FONT>';
  latexdef "rh" as "\rho";
htmldef "mu" as "<IMG SRC='_mu.gif' WIDTH=10 HEIGHT=19 TITLE='mu' ALIGN=TOP>";
  althtmldef "mu" as '<FONT COLOR="#0000FF"><I>&mu;</I></FONT>';
  latexdef "mu" as "\rho";
htmldef "la" as
    "<IMG SRC='_lambda.gif' WIDTH=9 HEIGHT=19 TITLE='la' ALIGN=TOP>";
  althtmldef "la" as '<FONT COLOR="#0000FF"><I>&lambda;</I></FONT>';
  latexdef "la" as "\rho";
htmldef "ka" as
    "<IMG SRC='_kappa.gif' WIDTH=9 HEIGHT=19 TITLE='ka' ALIGN=TOP>";
  althtmldef "ka" as '<FONT COLOR="#0000FF"><I>&kappa;</I></FONT>';
  latexdef "ka" as "\rho";
htmldef "<->" as " <IMG SRC='leftrightarrow.gif' WIDTH=15 HEIGHT=19 " +
    "TITLE='&lt;-&gt;' ALIGN=TOP> ";
  althtmldef "<->" as ' &harr; ';
  latexdef "<->" as "\leftrightarrow";
htmldef "\/" as
     " <IMG SRC='vee.gif' WIDTH=11 HEIGHT=19 TITLE='\/' ALIGN=TOP> ";
  althtmldef "\/" as ' <FONT FACE=sans-serif> &or;</FONT> ' ;
    /* althtmldef "\/" as ' <FONT FACE=sans-serif>&#8897;</FONT> ' ; */
    /* was &or; - changed to match font of &and; replacement */
    /* Changed back 6-Mar-2012 NM */
  latexdef "\/" as "\vee";
htmldef "/\" as
    " <IMG SRC='wedge.gif' WIDTH=11 HEIGHT=19 TITLE='/\' ALIGN=TOP> ";
  althtmldef "/\" as ' <FONT FACE=sans-serif>&and;</FONT> ';
    /* althtmldef "/\" as ' <FONT FACE=sans-serif>&#8896;</FONT> '; */
    /* was &and; which is circle in Mozilla on WinXP Pro (but not Home) */
    /* Changed back 6-Mar-2012 NM */
  latexdef "/\" as "\wedge";
htmldef "A." as
    "<IMG SRC='forall.gif' WIDTH=10 HEIGHT=19 TITLE='A.' ALIGN=TOP>";
  althtmldef "A." as '<FONT FACE=sans-serif>&forall;</FONT>'; /* &#8704; */
  latexdef "A." as "\forall";
htmldef "setvar" as
    "<IMG SRC='_setvar.gif' WIDTH=40 HEIGHT=19 ALT=' setvar' TITLE='setvar'> ";
  althtmldef
    "setvar" as '<SPAN CLASS=typecode STYLE="color:gray">setvar </SPAN>';
  latexdef "setvar" as "{\rm setvar}";
htmldef "x" as "<IMG SRC='_x.gif' WIDTH=10 HEIGHT=19 TITLE='x' ALIGN=TOP>";
  althtmldef "x" as '<I><FONT COLOR="#FF0000">x</FONT></I>';
  latexdef "x" as "x";
htmldef "y" as "<IMG SRC='_y.gif' WIDTH=9 HEIGHT=19 TITLE='y' ALIGN=TOP>";
  althtmldef "y" as '<I><FONT COLOR="#FF0000">y</FONT></I>';
  latexdef "y" as "y";
htmldef "z" as "<IMG SRC='_z.gif' WIDTH=9 HEIGHT=19 TITLE='z' ALIGN=TOP>";
  althtmldef "z" as '<I><FONT COLOR="#FF0000">z</FONT></I>';
  latexdef "z" as "z";
htmldef "w" as "<IMG SRC='_w.gif' WIDTH=12 HEIGHT=19 TITLE='w' ALIGN=TOP>";
  althtmldef "w" as '<I><FONT COLOR="#FF0000">w</FONT></I>';
  latexdef "w" as "w";
htmldef "v" as "<IMG SRC='_v.gif' WIDTH=9 HEIGHT=19 TITLE='v' ALIGN=TOP>";
  althtmldef "v" as '<I><FONT COLOR="#FF0000">v</FONT></I>';
  latexdef "v" as "v";
htmldef "u" as "<IMG SRC='_u.gif' WIDTH=10 HEIGHT=19 TITLE='u' ALIGN=TOP>";
  althtmldef "u" as '<I><FONT COLOR="#FF0000">u</FONT></I>';
  latexdef "u" as "u";
htmldef "t" as "<IMG SRC='_t.gif' WIDTH=7 HEIGHT=19 ALT=' t' TITLE='t'>";
  althtmldef "t" as '<SPAN CLASS=set STYLE="color:red">&#x1D461;</SPAN>';
  latexdef "t" as "t";
htmldef "E." as
    "<IMG SRC='exists.gif' WIDTH=9 HEIGHT=19 TITLE='E.' ALIGN=TOP>";
  althtmldef "E." as '<FONT FACE=sans-serif>&exist;</FONT>'; /* &#8707; */
    /* Without sans-serif, bad in Opera and way too big in FF3 */
  latexdef "E." as "\exists";
htmldef "F/" as
    "<IMG SRC='finv.gif' WIDTH=9 HEIGHT=19 ALT=' F/' TITLE='F/'>";
  althtmldef "F/" as "&#8498;";
  latexdef "F/" as "\Finv";
htmldef "=" as " <IMG SRC='eq.gif' WIDTH=12 HEIGHT=19 TITLE='=' ALIGN=TOP> ";
  althtmldef "=" as ' = '; /* &equals; */
  latexdef "=" as "=";
htmldef "e." as " <IMG SRC='in.gif' WIDTH=10 HEIGHT=19 TITLE='e.' ALIGN=TOP> ";
  althtmldef "e." as ' <FONT FACE=sans-serif>&isin;</FONT> ';
  latexdef "e." as "\in";
htmldef "[" as "<IMG SRC='lbrack.gif' WIDTH=5 HEIGHT=19 TITLE='[' ALIGN=TOP>";
  althtmldef "[" as '['; /* &lsqb; */
  latexdef "[" as "[";
htmldef "/" as
    " <IMG SRC='solidus.gif' WIDTH=6 HEIGHT=19 TITLE='/' ALIGN=TOP> ";
  althtmldef "/" as ' / '; /* &sol; */
  latexdef "/" as "/";
htmldef "]" as "<IMG SRC='rbrack.gif' WIDTH=5 HEIGHT=19 TITLE=']' ALIGN=TOP>";
  althtmldef "]" as ']'; /* &rsqb; */
  latexdef "]" as "]";
htmldef "E!" as "<IMG SRC='_e1.gif' WIDTH=12 HEIGHT=19 TITLE='E!' ALIGN=TOP>";
  althtmldef "E!" as '<FONT FACE=sans-serif>&exist;!</FONT>';
  latexdef "E!" as "\exists{!}";
htmldef "E*" as "<IMG SRC='_em1.gif' WIDTH=15 HEIGHT=19 TITLE='E*' ALIGN=TOP>";
  althtmldef "E*" as '<FONT FACE=sans-serif>&exist;*</FONT>';
  latexdef "E*" as "\exists^\ast";
htmldef "{" as "<IMG SRC='lbrace.gif' WIDTH=6 HEIGHT=19 ALT=' {' TITLE='{'>";
  althtmldef "{" as '{'; /* &lcub; */
  latexdef "{" as "\{";
htmldef "|" as " <IMG SRC='vert.gif' WIDTH=3 HEIGHT=19 ALT=' |' TITLE='|'> ";
  althtmldef "|" as ' &#8739; '; /* &vertbar; */
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "|" as "|";
htmldef "}" as "<IMG SRC='rbrace.gif' WIDTH=6 HEIGHT=19 ALT=' }' TITLE='}'>";
  althtmldef "}" as '}'; /* &rcub; */
  latexdef "}" as "\}";
htmldef "F/_" as
    "<IMG SRC='_finvbar.gif' WIDTH=9 HEIGHT=19 ALT=' F/_' TITLE='F/_'>";
  althtmldef "F/_" as "<U>&#8498;</U>";
  latexdef "F/_" as "\underline{\Finv}";
htmldef "CondEq" as "CondEq";
  althtmldef "CondEq" as "CondEq";
  latexdef "CondEq" as "\mbox{CondEq}";
htmldef "./\" as
    " <IMG SRC='_.wedge.gif' WIDTH=11 HEIGHT=19 ALT=' ./\' TITLE='./\'> ";
  althtmldef "./\" as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '&and;</SPAN> ';
  latexdef "./\" as "\wedge";
htmldef ".\/" as
    " <IMG SRC='_.vee.gif' WIDTH=11 HEIGHT=19 ALT=' .\/' TITLE='.\/'> ";
  althtmldef ".\/" as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '&or;</SPAN> ';
  latexdef ".\/" as "\vee";
htmldef ".<_" as
    " <IMG SRC='_.le.gif' WIDTH=11 HEIGHT=19 ALT=' .&lt;_' TITLE='.&lt;_'> ";
  althtmldef ".<_" as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '&le;</SPAN> ';
  latexdef ".<_" as "\le";
htmldef ".<" as     /* Symbol as variable */
    " <IMG SRC='_.lt.gif' WIDTH=11 HEIGHT=19 ALT=' .&lt;' TITLE='.&lt;'> ";
  althtmldef ".<" as
    /* This is how to put a dotted box around the symbol: */
    /* border means box around symbol; border-bottom underlines symbol */
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '&lt;</SPAN> ';
    /* Todo: can this STYLE sequence be done with a CLASS? */
    /* Move the underline down 3px so it isn't too close to symbol */
    /*
    ' <SPAN STYLE="vertical-align:-3px">' +
    '<SPAN CLASS=symvar STYLE="text-decoration:underline dotted;color:#C3C">' +
    '<SPAN STYLE="vertical-align:3px">&lt;</SPAN></SPAN></SPAN> ';
    */
  latexdef ".<" as "<";
htmldef ".+" as
    " <IMG SRC='_.plus.gif' WIDTH=13 HEIGHT=19 ALT=' .+' TITLE='.+'> ";
  althtmldef ".+" as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '+</SPAN> ';
  latexdef ".+" as "+";
htmldef ".-" as
    " <IMG SRC='_.minus.gif' WIDTH=11 HEIGHT=19 ALT=' .-' TITLE='.-'> ";
  althtmldef ".-" as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '&minus;</SPAN> ';
  latexdef ".-" as "-";
htmldef ".X." as
    " <IMG SRC='_.times.gif' WIDTH=9 HEIGHT=19 ALT=' .X.' TITLE='.X.'> ";
  althtmldef ".X." as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '&times;</SPAN> ';
  latexdef ".X." as "\times";
htmldef "./" as
    " <IMG SRC='_.solidus.gif' WIDTH=8 HEIGHT=19 ALT=' ./' TITLE='./'> ";
  althtmldef "./" as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '/</SPAN> ';
  latexdef "./" as "/";
htmldef ".^" as
    " <IMG SRC='_.uparrow.gif' WIDTH=7 HEIGHT=19 ALT=' .^' TITLE='.^'> ";
  althtmldef ".^" as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '&uarr;</SPAN> ';
  latexdef ".^" as "\uparrow";
htmldef ".0." as
    " <IMG SRC='_.0.gif' WIDTH=8 HEIGHT=19 ALT=' .0.' TITLE='.0.'> ";
  althtmldef ".0." as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '0</SPAN> ';
  latexdef ".0." as "0";
htmldef ".1." as
    " <IMG SRC='_.1.gif' WIDTH=7 HEIGHT=19 ALT=' .1.' TITLE='.1.'> ";
  althtmldef ".1." as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '1</SPAN> ';
  latexdef ".1." as "1";
htmldef ".||" as
    " <IMG SRC='_.parallel.gif' WIDTH=5 HEIGHT=19 ALT=' .||' TITLE='.||'> ";
  althtmldef ".||" as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '&#8741;</SPAN> ';
  latexdef ".||" as "\parallel";
htmldef ".~" as
    " <IMG SRC='_.sim.gif' WIDTH=13 HEIGHT=19 ALT=' .~' TITLE='.~'> ";
  althtmldef ".~" as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '&#x223C;</SPAN> ';
  latexdef ".~" as "\sim";
htmldef "._|_" as
    " <IMG SRC='_.perp.gif' WIDTH=11 HEIGHT=19 ALT=' ._|_' TITLE='._|_'> ";
  althtmldef "._|_" as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '&#8869;</SPAN> ';
  latexdef "._|_" as "\perp";
htmldef ".+^" as
    " <IMG SRC='_.plushat.gif' WIDTH=11 HEIGHT=19 ALT=' .+^' TITLE='.+^'> ";
  althtmldef ".+^" as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '&#x2A23;</SPAN> ';       /* &plusacir; */
  latexdef ".+^" as "\hat{+}";
htmldef ".+b" as
    " <IMG SRC='_.plusb.gif' WIDTH=14 HEIGHT=19 ALT=' .+b' TITLE='.+b'> ";
  althtmldef ".+b" as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '&#x271A;</SPAN> ';
  latexdef ".+b" as "\boldsymbol{+}";
htmldef ".(+)" as
    " <IMG SRC='_.oplus.gif' WIDTH=13 HEIGHT=19 ALT=' .(+)' TITLE='.(+)'> ";
  althtmldef ".(+)" as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '&#x2295;</SPAN> ';
  latexdef ".(+)" as "\oplus";
htmldef ".*" as
    " <IMG SRC='_.ast.gif' WIDTH=7 HEIGHT=19 ALT=' .*' TITLE='.*'> ";
  althtmldef ".*" as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '&lowast;</SPAN> ';
  latexdef ".*" as "\ast";
htmldef ".x." as
    " <IMG SRC='_.cdot.gif' WIDTH=4 HEIGHT=19 ALT=' .x.' TITLE='.x.'> ";
  althtmldef ".x." as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '&middot;</SPAN> ';
  latexdef ".x." as "\cdot";
htmldef ".xb" as
    " <IMG SRC='_.bullet.gif' WIDTH=8 HEIGHT=19 ALT=' .xb' TITLE='.xb'> ";
  althtmldef ".xb" as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '&#x2219;</SPAN> ';
  latexdef ".xb" as "\bullet";
htmldef ".," as
    " <IMG SRC='_.comma.gif' WIDTH=4 HEIGHT=19 ALT=' .,' TITLE='.,'> ";
  althtmldef ".," as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    ',</SPAN> ';
  latexdef ".," as ",";
htmldef ".(x)" as
    " <IMG SRC='_.otimes.gif' WIDTH=13 HEIGHT=19 ALT=' .(x)' TITLE='.(x)'> ";
  althtmldef ".(x)" as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '&#x2297;</SPAN> ';
  latexdef ".(x)" as "\otimes";
htmldef ".0b" as
    " <IMG SRC='_.bf0.gif' WIDTH=9 HEIGHT=19 ALT=' .0b' TITLE='.0b'> ";
  althtmldef ".0b" as
    ' <SPAN CLASS=symvar STYLE="border-bottom:1px dotted;color:#C3C">' +
    '&#x1D7CE</SPAN> ';
  latexdef ".0b" as "\mbox{\boldmath$0$}";

/* "~P" was deleted from above section in set.mm. */
/* The ones below should have been in the above section in set.mm. */
htmldef "class" as
    "<IMG SRC='_class.gif' WIDTH=32 HEIGHT=19 TITLE='class' ALIGN=TOP> ";
  althtmldef "class" as '<FONT COLOR="#808080">class </FONT>';
  latexdef "class" as "{\rm class}";
htmldef "A" as "<IMG SRC='_ca.gif' WIDTH=11 HEIGHT=19 TITLE='A' ALIGN=TOP>";
  althtmldef "A" as '<I><FONT COLOR="#CC33CC">A</FONT></I>';
  latexdef "A" as "A";
htmldef "B" as "<IMG SRC='_cb.gif' WIDTH=12 HEIGHT=19 TITLE='B' ALIGN=TOP>";
  althtmldef "B" as '<I><FONT COLOR="#CC33CC">B</FONT></I>';
  latexdef "B" as "B";
htmldef "C" as "<IMG SRC='_cc.gif' WIDTH=12 HEIGHT=19 ALT=' C' TITLE='C'>";
  althtmldef "C" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D436;</SPAN>';
  latexdef "C" as "C";
htmldef "D" as "<IMG SRC='_cd.gif' WIDTH=12 HEIGHT=19 ALT=' D' TITLE='D'>";
  althtmldef "D" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D437;</SPAN>';
  latexdef "D" as "D";
htmldef "P" as "<IMG SRC='_cp.gif' WIDTH=12 HEIGHT=19 ALT=' P' TITLE='P'>";
  althtmldef "P" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D443;</SPAN>';
  latexdef "P" as "P";
htmldef "Q" as "<IMG SRC='_cq.gif' WIDTH=12 HEIGHT=19 ALT=' Q' TITLE='Q'>";
  althtmldef "Q" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D444;</SPAN>';
  latexdef "Q" as "Q";
htmldef "R" as "<IMG SRC='_cr.gif' WIDTH=12 HEIGHT=19 ALT=' R' TITLE='R'>";
  althtmldef "R" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D445;</SPAN>';
  latexdef "R" as "R";
htmldef "S" as "<IMG SRC='_cs.gif' WIDTH=11 HEIGHT=19 ALT=' S' TITLE='S'>";
  althtmldef "S" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D446;</SPAN>';
  latexdef "S" as "S";
htmldef "T" as "<IMG SRC='_ct.gif' WIDTH=12 HEIGHT=19 ALT=' T' TITLE='T'>";
  althtmldef "T" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D447;</SPAN>';
  latexdef "T" as "T";
htmldef "U" as "<IMG SRC='_cu.gif' WIDTH=12 HEIGHT=19 ALT=' U' TITLE='U'>";
  althtmldef "U" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D448;</SPAN>';
  latexdef "U" as "U";
htmldef "e" as "<IMG SRC='_e.gif' WIDTH=8 HEIGHT=19 ALT=' e' TITLE='e'>";
  althtmldef "e" as '<SPAN CLASS=set STYLE="color:red">&#x1D452;</SPAN>';
  latexdef "e" as "e";
htmldef "f" as "<IMG SRC='_f.gif' WIDTH=9 HEIGHT=19 TITLE='f' ALIGN=TOP>";
  althtmldef "f" as '<I><FONT COLOR="#FF0000">f</FONT></I>';
  latexdef "f" as "f";
htmldef "g" as "<IMG SRC='_g.gif' WIDTH=9 HEIGHT=19 TITLE='g' ALIGN=TOP>";
  althtmldef "g" as '<I><FONT COLOR="#FF0000">g</FONT></I>';
  latexdef "g" as "g";
htmldef "h" as "<IMG SRC='_h.gif' WIDTH=10 HEIGHT=19 ALT=' h' TITLE='h'>";
  althtmldef "h" as '<SPAN CLASS=set STYLE="color:red">&#x210E;</SPAN>';
  latexdef "h" as "h";
htmldef "i" as "<IMG SRC='_i.gif' WIDTH=6 HEIGHT=19 ALT=' i' TITLE='i'>";
  althtmldef "i" as '<SPAN CLASS=set STYLE="color:red">&#x1D456;</SPAN>';
  latexdef "i" as "i";
htmldef "j" as "<IMG SRC='_j.gif' WIDTH=7 HEIGHT=19 ALT=' j' TITLE='j'>";
  althtmldef "j" as '<SPAN CLASS=set STYLE="color:red">&#x1D457;</SPAN>';
  latexdef "j" as "j";
htmldef "k" as "<IMG SRC='_k.gif' WIDTH=9 HEIGHT=19 ALT=' k' TITLE='k'>";
  althtmldef "k" as '<SPAN CLASS=set STYLE="color:red">&#x1D458;</SPAN>';
  latexdef "k" as "k";
htmldef "m" as "<IMG SRC='_m.gif' WIDTH=14 HEIGHT=19 ALT=' m' TITLE='m'>";
  althtmldef "m" as '<SPAN CLASS=set STYLE="color:red">&#x1D45A;</SPAN>';
  latexdef "m" as "m";
htmldef "n" as "<IMG SRC='_n.gif' WIDTH=10 HEIGHT=19 ALT=' n' TITLE='n'>";
  althtmldef "n" as '<SPAN CLASS=set STYLE="color:red">&#x1D45B;</SPAN>';
  latexdef "n" as "n";
htmldef "o" as "<IMG SRC='_o.gif' WIDTH=8 HEIGHT=19 ALT=' o' TITLE='o'>";
  althtmldef "o" as '<SPAN CLASS=set STYLE="color:red">&#x1D45C;</SPAN>';
  latexdef "o" as "o";
htmldef "E" as "<IMG SRC='_ce.gif' WIDTH=13 HEIGHT=19 ALT=' E' TITLE='E'>";
  althtmldef "E" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D438;</SPAN>';
  latexdef "E" as "E";
htmldef "F" as "<IMG SRC='_cf.gif' WIDTH=13 HEIGHT=19 ALT=' F' TITLE='F'>";
  althtmldef "F" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D439;</SPAN>';
  latexdef "F" as "F";
htmldef "G" as "<IMG SRC='_cg.gif' WIDTH=12 HEIGHT=19 ALT=' G' TITLE='G'>";
  althtmldef "G" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D43A;</SPAN>';
  latexdef "G" as "G";
htmldef "H" as "<IMG SRC='_ch.gif' WIDTH=14 HEIGHT=19 ALT=' H' TITLE='H'>";
  althtmldef "H" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D43B;</SPAN>';
  latexdef "H" as "H";
htmldef "I" as "<IMG SRC='_ci.gif' WIDTH=8 HEIGHT=19 ALT=' I' TITLE='I'>";
  althtmldef "I" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D43C;</SPAN>';
  latexdef "I" as "I";
htmldef "J" as "<IMG SRC='_cj.gif' WIDTH=10 HEIGHT=19 ALT=' J' TITLE='J'>";
  althtmldef "J" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D43D;</SPAN>';
  latexdef "J" as "J";
htmldef "K" as "<IMG SRC='_ck.gif' WIDTH=14 HEIGHT=19 ALT=' K' TITLE='K'>";
  althtmldef "K" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D43E;</SPAN>';
  latexdef "K" as "K";
htmldef "L" as "<IMG SRC='_cl.gif' WIDTH=10 HEIGHT=19 ALT=' L' TITLE='L'>";
  althtmldef "L" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D43F;</SPAN>';
  latexdef "L" as "L";
htmldef "M" as "<IMG SRC='_cm.gif' WIDTH=15 HEIGHT=19 ALT=' M' TITLE='M'>";
  althtmldef "M" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D440;</SPAN>';
  latexdef "M" as "M";
htmldef "N" as "<IMG SRC='_cn.gif' WIDTH=14 HEIGHT=19 ALT=' N' TITLE='N'>";
  althtmldef "N" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D441;</SPAN>';
  latexdef "N" as "N";
htmldef "V" as "<IMG SRC='_cv.gif' WIDTH=12 HEIGHT=19 ALT=' V' TITLE='V'>";
  althtmldef "V" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D449;</SPAN>';
  latexdef "V" as "V";
htmldef "W" as "<IMG SRC='_cw.gif' WIDTH=16 HEIGHT=19 ALT=' W' TITLE='W'>";
  althtmldef "W" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D44A;</SPAN>';
  latexdef "W" as "W";
htmldef "X" as "<IMG SRC='_cx.gif' WIDTH=13 HEIGHT=19 ALT=' X' TITLE='X'>";
  althtmldef "X" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D44B;</SPAN>';
  latexdef "X" as "X";
htmldef "Y" as "<IMG SRC='_cy.gif' WIDTH=12 HEIGHT=19 ALT=' Y' TITLE='Y'>";
  althtmldef "Y" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D44C;</SPAN>';
  latexdef "Y" as "Y";
htmldef "Z" as "<IMG SRC='_cz.gif' WIDTH=11 HEIGHT=19 ALT=' Z' TITLE='Z'>";
  althtmldef "Z" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D44D;</SPAN>';
  latexdef "Z" as "Z";
htmldef "O" as "<IMG SRC='_co.gif' WIDTH=12 HEIGHT=19 ALT=' O' TITLE='O'>";
  althtmldef "O" as '<SPAN CLASS=class STYLE="color:#C3C">&#x1D442;</SPAN>';
  latexdef "O" as "O";
htmldef "s" as "<IMG SRC='_s.gif' WIDTH=7 HEIGHT=19 ALT=' s' TITLE='s'>";
  althtmldef "s" as '<SPAN CLASS=set STYLE="color:red">&#x1D460;</SPAN>';
  latexdef "s" as "s";
htmldef "r" as "<IMG SRC='_r.gif' WIDTH=8 HEIGHT=19 ALT=' r' TITLE='r'>";
  althtmldef "r" as '<SPAN CLASS=set STYLE="color:red">&#x1D45F;</SPAN>';
  latexdef "r" as "r";
htmldef "q" as "<IMG SRC='_q.gif' WIDTH=8 HEIGHT=19 ALT=' q' TITLE='q'>";
  althtmldef "q" as '<SPAN CLASS=set STYLE="color:red">&#x1D45E;</SPAN>';
  latexdef "q" as "q";
htmldef "p" as "<IMG SRC='_p.gif' WIDTH=10 HEIGHT=19 ALT=' p' TITLE='p'>";
  althtmldef "p" as '<SPAN CLASS=set STYLE="color:red">&#x1D45D;</SPAN>';
  latexdef "p" as "p";
htmldef "a" as "<IMG SRC='_a.gif' WIDTH=9 HEIGHT=19 ALT=' a' TITLE='a'>";
  althtmldef "a" as '<SPAN CLASS=set STYLE="color:red">&#x1D44E;</SPAN>';
  latexdef "a" as "a";
htmldef "b" as "<IMG SRC='_b.gif' WIDTH=8 HEIGHT=19 ALT=' b' TITLE='b'>";
  althtmldef "b" as '<SPAN CLASS=set STYLE="color:red">&#x1D44F;</SPAN>';
  latexdef "b" as "b";
htmldef "c" as "<IMG SRC='_c.gif' WIDTH=7 HEIGHT=19 ALT=' c' TITLE='c'>";
  althtmldef "c" as '<SPAN CLASS=set STYLE="color:red">&#x1D450;</SPAN>';
  latexdef "c" as "c";
htmldef "d" as "<IMG SRC='_d.gif' WIDTH=9 HEIGHT=19 ALT=' d' TITLE='d'>";
  althtmldef "d" as '<SPAN CLASS=set STYLE="color:red">&#x1D451;</SPAN>';
  latexdef "d" as "d";
htmldef "l" as "<IMG SRC='_l.gif' WIDTH=6 HEIGHT=19 ALT=' l' TITLE='l'>";
  althtmldef "l" as '<SPAN CLASS=set STYLE="color:red">&#x1D459;</SPAN>';
  latexdef "l" as "l";
htmldef "=/=" as
    " <IMG SRC='ne.gif' WIDTH=12 HEIGHT=19 ALT=' =/=' TITLE='=/='> ";
  althtmldef "=/=" as ' &ne; ';
  latexdef "=/=" as "\ne";
htmldef "e/" as
    " <IMG SRC='notin.gif' WIDTH=10 HEIGHT=19 ALT=' e/' TITLE='e/'> ";
  althtmldef "e/" as ' &notin; ';
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "e/" as "\notin";
htmldef "_V" as "<IMG SRC='rmcv.gif' WIDTH=10 HEIGHT=19 ALT=' _V' TITLE='_V'>";
  althtmldef "_V" as 'V';
  latexdef "_V" as "{\rm V}";
htmldef "[." as
    "<IMG SRC='_dlbrack.gif' WIDTH=7 HEIGHT=19 ALT=' [.' TITLE='[.'>";
  /* althtmldef "[." as '&#x298F;'; */   /* corner tick */
  /* althtmldef "[." as '[&#803;'; */  /* combining dot below */
  althtmldef "[." as '<B>[</B>'; /* 6-Aug-2018 nm */
  /* \underaccent is in accents package */
  latexdef "[." as "\underaccent{\dot}{[}";
htmldef "]." as
    "<IMG SRC='_drbrack.gif' WIDTH=6 HEIGHT=19 ALT=' ].' TITLE='].'>";
  /* althtmldef "]." as '&#x298E;'; */   /* corner tick */
  /* althtmldef "]." as ']&#803;'; */  /* combining dot below */
  althtmldef "]." as '<B>]</B>'; /* 6-Aug-2018 nm */
  latexdef "]." as "\underaccent{\dot}{]}";
htmldef "[_" as
    "<IMG SRC='_ulbrack.gif' WIDTH=7 HEIGHT=19 ALT=' [_' TITLE='[_'>";
  althtmldef "[_" as '<B>&#x298B;</B>'; /* left sq brack w underbar */
  latexdef "[_" as "\underline{[}";
htmldef "]_" as
    "<IMG SRC='_urbrack.gif' WIDTH=6 HEIGHT=19 ALT=' ]_' TITLE=']_'>";
  althtmldef "]_" as '<B>&#x298C;</B>'; /* right sq brack w underbar */
  latexdef "]_" as "\underline{]}";
htmldef "\" as
    " <IMG SRC='setminus.gif' WIDTH=8 HEIGHT=19 ALT=' \' TITLE='\'> ";
  althtmldef "\" as ' &#8726; '; /* &setmn; */
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "\" as "\setminus";
htmldef "u." as
    " <IMG SRC='cup.gif' WIDTH=10 HEIGHT=19 ALT=' u.' TITLE='u.'> ";
  althtmldef "u." as ' &cup; ';
  latexdef "u." as "\cup";
htmldef "i^i" as
    " <IMG SRC='cap.gif' WIDTH=10 HEIGHT=19 ALT=' i^i' TITLE='i^i'> ";
  althtmldef "i^i" as ' &cap; ';
  latexdef "i^i" as "\cap";
htmldef "C_" as
    " <IMG SRC='subseteq.gif' WIDTH=12 HEIGHT=19 ALT=' C_' TITLE='C_'> ";
  althtmldef "C_" as ' &#8838; '; /* &subE; */
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "C_" as "\subseteq";
    /* 7-Jun-2019 changed gif, unicode and latex def of "C." from subset to
    subsetneq (BJ) */
htmldef "C." as
    " <IMG SRC='subsetneq.gif' WIDTH=12 HEIGHT=19 ALT=' C.' TITLE='C.'> ";
    /* subset.gif */
  althtmldef "C." as ' &#x228a; '; /* &sub; */
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "C." as "\subsetneq"; /* \subset */
htmldef "(/)" as
    "<IMG SRC='varnothing.gif' WIDTH=11 HEIGHT=19 ALT=' (/)' TITLE='(/)'>";
  althtmldef "(/)" as '&empty;';
    /*althtmldef "(/)" as '&empty;';*/ /* =&#8709 */ /* bad in Opera */
    /*althtmldef "(/)" as '&#8960;';*/
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "(/)" as "\varnothing";
htmldef "if" as "<IMG SRC='_if.gif' WIDTH=11 HEIGHT=19 ALT=' if' TITLE='if'>";
  althtmldef "if" as 'if';
  latexdef "if" as "{\rm if}";
htmldef "," as "<IMG SRC='comma.gif' WIDTH=4 HEIGHT=19 ALT=' ,' TITLE=','> ";
  althtmldef "," as ', ';
  latexdef "," as ",";
htmldef "~P" as "<IMG SRC='scrp.gif' WIDTH=16 HEIGHT=19 ALT=' ~P' TITLE='~P'>";
  /* 4-Aug-2016 NM Put space after ~P, needed for e.g. ncanth where it
     overlapped the _V */
  althtmldef "~P" as '&#119979; ';
  latexdef "~P" as "{\cal P}";
htmldef "<." as
    "<IMG SRC='langle.gif' WIDTH=4 HEIGHT=19 ALT=' &lt;.' TITLE='&lt;.'>";
    /* The Unicode below doesn't always work on Firefox and Chrome on Windows,
       so revert to the image above */
  althtmldef "<." as '&lang;'; /* &#9001; */
    /* 2-Jan-2016 restored Unicode; reverted sans-serif */
  latexdef "<." as "\langle";
htmldef ">." as
    "<IMG SRC='rangle.gif' WIDTH=4 HEIGHT=19 ALT=' &gt;.' TITLE='&gt;.'>";
    /* The Unicode below doesn't always work on Firefox and Chrome on Windows,
       so revert to the image above */
  althtmldef ">." as '&rang;'; /* &#9002; */
    /* 2-Jan-2016 restored Unicode; reverted sans-serif */
  latexdef ">." as "\rangle";
htmldef "U." as
    "<IMG SRC='bigcup.gif' WIDTH=13 HEIGHT=19 ALT=' U.' TITLE='U.'>";
  /* 20-Sep-2017 nm Add space after U. in althtmldef to improve "U. ran" */
  althtmldef "U." as '<FONT SIZE="+1">&cup;</FONT> '; /* &xcup; */
    /* xcup does not render, and #8899 renders as a small bold cup, on
       Mozilla 1.7.3 on Windows XP */
    /*althtmldef "U." as '&#8899;';*/ /* &xcup; */
  latexdef "U." as "\bigcup";
htmldef "|^|" as
    "<IMG SRC='bigcap.gif' WIDTH=13 HEIGHT=19 ALT=' |^|' TITLE='|^|'>";
  /* 20-Sep-2017 nm Add space after |^| in althtmldef to improve "|^| ran" */
  althtmldef "|^|" as '<FONT SIZE="+1">&cap;</FONT> '; /* &xcap; */
    /*althtmldef "|^|" as '&#8898;';*/ /* &xcap; */
  latexdef "|^|" as "\bigcap";
htmldef "U_" as
    "<IMG SRC='_cupbar.gif' WIDTH=13 HEIGHT=19 ALT=' U_' TITLE='U_'>";
  /* 20-Sep-2017 nm Add space after U_ in althtmldef to improve "U_ ran" */
  althtmldef "U_" as '<U><FONT SIZE="+1">&cup;</FONT></U> '; /* &xcup; */
  latexdef "U_" as "\underline{\bigcup}";
htmldef "|^|_" as
    "<IMG SRC='_capbar.gif' WIDTH=13 HEIGHT=19 ALT=' |^|_' TITLE='|^|_'>";
  /* 20-Sep-2017 nm Add space after |^|_ in althtmldef to improve "|^|_ ran" */
  althtmldef "|^|_" as '<U><FONT SIZE="+1">&cap;</FONT></U> '; /* &xcap; */
  latexdef "|^|_" as "\underline{\bigcap}";
htmldef "Disj_" as "<u>Disj</u> ";
  althtmldef "Disj_" as "<u>Disj</u> ";
  latexdef "Disj_" as "\operatorname{\underline{Disj}}";
htmldef "|->" as " <IMG SRC='mapsto.gif' WIDTH=15 HEIGHT=19 ALT=' |-&gt;'" +
    " TITLE='|-&gt;'> ";
  althtmldef "|->" as ' &#8614; ';
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "|->" as "\mapsto";
htmldef "Tr" as
    "<IMG SRC='_ctr.gif' WIDTH=16 HEIGHT=19 ALT=' Tr' TITLE='Tr'> ";
  althtmldef "Tr" as 'Tr ';
  latexdef "Tr" as "{\rm Tr}";
htmldef "_E" as
    " <IMG SRC='rmce.gif' WIDTH=9 HEIGHT=19 ALT=' _E' TITLE='_E'> ";
  althtmldef "_E" as ' E ';
  latexdef "_E" as "{\rm E}";
htmldef "_I" as
    " <IMG SRC='rmci.gif' WIDTH=4 HEIGHT=19 ALT=' _I' TITLE='_I'> ";
  althtmldef "_I" as ' I ';
  latexdef "_I" as "{\rm I}";
htmldef "Po" as
    " <IMG SRC='_po.gif' WIDTH=16 HEIGHT=19 ALT=' Po' TITLE='Po'> ";
  althtmldef "Po" as ' Po ';
  latexdef "Po" as "{\rm Po}";
htmldef "Or" as
    " <IMG SRC='_or.gif' WIDTH=18 HEIGHT=19 ALT=' Or' TITLE='Or'> ";
  althtmldef "Or" as ' Or ';
  latexdef "Or" as "{\rm Or}";
htmldef "Se" as ' Se ';
  althtmldef "Se" as ' Se ';
  latexdef "Se" as "{\rm Se}";
htmldef "Ord" as
    "<IMG SRC='_ord.gif' WIDTH=26 HEIGHT=19 ALT=' Ord' TITLE='Ord'> ";
  althtmldef "Ord" as 'Ord ';
  latexdef "Ord" as "{\rm Ord}";
htmldef "On" as "<IMG SRC='_on.gif' WIDTH=20 HEIGHT=19 ALT=' On' TITLE='On'>";
  althtmldef "On" as 'On';
  latexdef "On" as "{\rm On}";
htmldef "Lim" as
    "<IMG SRC='_lim.gif' WIDTH=26 HEIGHT=19 ALT=' Lim' TITLE='Lim'> ";
  althtmldef "Lim" as 'Lim ';
  latexdef "Lim" as "{\rm Lim}";
htmldef "suc" as
    "<IMG SRC='_suc.gif' WIDTH=22 HEIGHT=19 ALT=' suc' TITLE='suc'> ";
  althtmldef "suc" as 'suc ';
  latexdef "suc" as "{\rm suc}";
htmldef "_om" as
    "<IMG SRC='omega.gif' WIDTH=11 HEIGHT=19 ALT=' om' TITLE='om'>";
  /*althtmldef "_om" as '&omega;';*/
  althtmldef "_om" as '&#x1D714;'; /* math italic omega */
  latexdef "_om" as "\omega";
htmldef "X." as
    " <IMG SRC='times.gif' WIDTH=9 HEIGHT=19 ALT=' X.' TITLE='X.'> ";
  althtmldef "X." as ' &times; ';
  latexdef "X." as "\times";
htmldef "~~" as
    " <IMG SRC='approx.gif' WIDTH=13 HEIGHT=19 ALT=' ~~' TITLE='~~'> ";
  althtmldef "~~" as ' &#8776; '; /* &ap; */
  latexdef "~~" as "\approx";
htmldef "~<_" as
   " <IMG SRC='preccurlyeq.gif' WIDTH=11 HEIGHT=19 " +
    "ALT=' ~&lt;_' TITLE='~&lt;_'> ";
  althtmldef "~<_" as ' &#8828; '; /* &prcue; */
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "~<_" as "\preccurlyeq";
htmldef "Fin" as
    "<IMG SRC='_fin.gif' WIDTH=21 HEIGHT=19 ALT=' Fin' TITLE='Fin'>";
  althtmldef "Fin" as 'Fin';
  latexdef "Fin" as "{\rm Fin}";
htmldef "`'" as "<IMG SRC='_cnv.gif' WIDTH=10 HEIGHT=19 ALT=" + '"' + " `'" +
    '"' + "TITLE=" + '"' + "`'" + '"' + ">";
  althtmldef "`'" as '<FONT SIZE="-1"><SUP>&#9697;</SUP></FONT>'; /* or 8995 */
  latexdef "`'" as "{}^{\smallsmile}";
htmldef "dom" as
    "<IMG SRC='_dom.gif' WIDTH=26 HEIGHT=19 ALT=' dom' TITLE='dom'> ";
  althtmldef "dom" as 'dom ';
  latexdef "dom" as "{\rm dom}";
htmldef "ran" as
    "<IMG SRC='_ran.gif' WIDTH=22 HEIGHT=19 ALT=' ran' TITLE='ran'> ";
  althtmldef "ran" as 'ran ';
  latexdef "ran" as "{\rm ran}";
htmldef "|`" as " <IMG SRC='restriction.gif' WIDTH=5 HEIGHT=19 ALT=' |`'" +
    " TITLE='|`'> ";
  althtmldef "|`" as ' &#8638; '; /* &uharr; */
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "|`" as "\restriction";
htmldef '"' as "<IMG SRC='backquote.gif' WIDTH=7 HEIGHT=19 ALT=' " + '"' +
    "' TITLE='" + '"' + "'>";
  althtmldef '"' as ' &ldquo; '; /* &#8220; */
  latexdef '"' as "``";
htmldef "o." as
    " <IMG SRC='circ.gif' WIDTH=8 HEIGHT=19 ALT=' o.' TITLE='o.'> ";
  althtmldef "o." as ' &#8728; ';
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "o." as "\circ";
htmldef "Rel" as
    "<IMG SRC='_rel.gif' WIDTH=22 HEIGHT=19 ALT=' Rel' TITLE='Rel'> ";
  althtmldef "Rel" as 'Rel ';
  latexdef "Rel" as "{\rm Rel}";
htmldef "iota" as
    "<IMG SRC='riota.gif' WIDTH=6 HEIGHT=19 ALT=' iota' TITLE='iota'>";
  althtmldef "iota" as '&#8489;';
  latexdef "iota" as "\mathrm{\rotatebox[origin=C]{180}{$\iota$}}";
htmldef "Smo" as
    "<IMG SRC='_smo.gif' WIDTH=27 HEIGHT=19 ALT=' Smo' TITLE='Smo'> ";
  althtmldef "Smo" as "Smo ";
  latexdef "Smo" as "{\rm Smo}";
htmldef "recs" as "recs";
  althtmldef "recs" as "recs";
  latexdef "recs" as "\mathrm{recs}";
htmldef "rec" as
    "<IMG SRC='_rec.gif' WIDTH=21 HEIGHT=19 ALT=' rec' TITLE='rec'>";
  althtmldef "rec" as 'rec';
  latexdef "rec" as "{\rm rec}";
htmldef "frec" as "frec";
  althtmldef "frec" as "frec";
  latexdef "frec" as "{\rm frec}";
htmldef "1o" as "<IMG SRC='_1o.gif' WIDTH=13 HEIGHT=19 ALT=' 1o' TITLE='1o'>";
  althtmldef "1o" as '1<SUB>&#x1D45C;</SUB>';
  latexdef "1o" as "1_o";
htmldef "2o" as "<IMG SRC='_2o.gif' WIDTH=14 HEIGHT=19 ALT=' 2o' TITLE='2o'>";
  althtmldef "2o" as '2<SUB>&#x1D45C;</SUB>';
  latexdef "2o" as "2_o";
htmldef "3o" as "<IMG SRC='_3o.gif' WIDTH=14 HEIGHT=19 ALT=' 3o' TITLE='3o'>";
  althtmldef "3o" as "3<SUB>&#x1D45C;</SUB>"; latexdef "3o" as "3_o";
htmldef "4o" as "<IMG SRC='_4o.gif' WIDTH=15 HEIGHT=19 ALT=' 4o' TITLE='4o'>";
  althtmldef "4o" as "4<SUB>&#x1D45C;</SUB>"; latexdef "4o" as "4_o";
htmldef "+o" as
    " <IMG SRC='_plo.gif' WIDTH=18 HEIGHT=19 ALT=' +o' TITLE='+o'> ";
  althtmldef "+o" as ' +<SUB>&#x1D45C;</SUB> ';
  latexdef "+o" as "+_o";
htmldef ".o" as
    " <IMG SRC='_cdo.gif' WIDTH=10 HEIGHT=19 ALT=' .o' TITLE='.o'> ";
  althtmldef ".o" as ' &middot;<SUB>&#x1D45C;</SUB> ';
  latexdef ".o" as "\cdot_o";
htmldef "^oi" as ' &uarr;<SUB>&#x1D45C;</SUB> ';
  althtmldef "^oi" as ' &uarr;<SUB>&#x1D45C;</SUB> ';
  latexdef "^oi" as "\uparrow_{oi}";
htmldef "Er" as
    " <IMG SRC='_er.gif' WIDTH=16 HEIGHT=19 ALT=' Er' TITLE='Er'> ";
  althtmldef "Er" as ' Er ';
  latexdef "Er" as "{\rm Er}";
htmldef "/." as
    "<IMG SRC='diagup.gif' WIDTH=14 HEIGHT=19 ALT=' /.' TITLE='/.'>";
  althtmldef "/." as ' <B>/</B> ';
  latexdef "/." as "\diagup";

htmldef "Fun" as
    "<IMG SRC='_fun.gif' WIDTH=25 HEIGHT=19 ALT=' Fun' TITLE='Fun'> ";
  althtmldef "Fun" as 'Fun ';
  latexdef "Fun" as "{\rm Fun}";
htmldef "Fn" as
    " <IMG SRC='_fn.gif' WIDTH=17 HEIGHT=19 ALT=' Fn' TITLE='Fn'> ";
  althtmldef "Fn" as ' Fn ';
  latexdef "Fn" as "{\rm Fn}";
htmldef ":" as "<IMG SRC='colon.gif' WIDTH=4 HEIGHT=19 ALT=' :' TITLE=':'>";
  althtmldef ":" as ':';
  latexdef ":" as ":";
htmldef "-->" as
  "<IMG SRC='longrightarrow.gif' WIDTH=23 HEIGHT=19 " +
    "ALT=' --&gt;' TITLE='--&gt;'>";
  /* althtmldef "-->" as '&ndash;&rarr;'; */
  althtmldef "-->" as '&#x27F6;';
    /* &#xAD;&#x2010;&ndash;&mdash;&minus; (possible symbols test) */
  latexdef "-->" as "\longrightarrow";
htmldef "-1-1->" as "<IMG SRC='onetoone.gif' WIDTH=23 HEIGHT=19 " +
    "ALT=' -1-1-&gt;' TITLE='-1-1-&gt;'>";
  althtmldef "-1-1->" as
    '&ndash;<FONT SIZE=-2 FACE=sans-serif>1-1</FONT>&rarr;';
  latexdef "-1-1->" as
    "\raisebox{.5ex}{${\textstyle{\:}_{\mbox{\footnotesize\rm 1" +
    "\tt -\rm 1}}}\atop{\textstyle{" +
    "\longrightarrow}\atop{\textstyle{}^{\mbox{\footnotesize\rm {\ }}}}}$}";
htmldef "-onto->" as
    "<IMG SRC='onto.gif' WIDTH=23 HEIGHT=19 " +
    "ALT=' -onto-&gt;' TITLE='-onto-&gt;'>";
  althtmldef "-onto->" as
    '&ndash;<FONT SIZE=-2 FACE=sans-serif>onto</FONT>&rarr;';
  latexdef "-onto->" as
    "\raisebox{.5ex}{${\textstyle{\:}_{\mbox{\footnotesize\rm {\ }}}}" +
    "\atop{\textstyle{" +
    "\longrightarrow}\atop{\textstyle{}^{\mbox{\footnotesize\rm onto}}}}$}";
htmldef "-1-1-onto->" as "<IMG SRC='onetooneonto.gif' WIDTH=23 HEIGHT=19 " +
    "ALT=' -1-1-onto-&gt;' TITLE='-1-1-onto-&gt;'>";
  althtmldef "-1-1-onto->" as '&ndash;<FONT SIZE=-2 '
    + 'FACE=sans-serif>1-1</FONT>-<FONT SIZE=-2 '
    + 'FACE=sans-serif>onto</FONT>&rarr;';
  latexdef "-1-1-onto->" as
    "\raisebox{.5ex}{${\textstyle{\:}_{\mbox{\footnotesize\rm 1" +
    "\tt -\rm 1}}}\atop{\textstyle{" +
    "\longrightarrow}\atop{\textstyle{}^{\mbox{\footnotesize\rm onto}}}}$}";
htmldef "`" as
    "<IMG SRC='backtick.gif' WIDTH=4 HEIGHT=19 ALT=' ` ' TITLE='` '>";
    /* Above, IE7 _printing_ is corrupted by '`'; use '` ' which works */
  /*althtmldef "`" as ' &lsquo;';*/
  /* I took out the leading space to make e.g. ( log ` A ) look better.
     I added the leading space a long time ago because the quote overlapped
     the character to its left, making it sometimes hidden, but that seems
     to be no longer a problem with the XITS font. - 29-Aug-2017 nm */
  althtmldef "`" as '&lsquo;';
  latexdef "`" as "`";
htmldef "Isom" as
    " <IMG SRC='_isom.gif' WIDTH=30 HEIGHT=19 ALT=' Isom' TITLE='Isom'> ";
  althtmldef "Isom" as ' Isom ';
  latexdef "Isom" as "{\rm Isom}";
htmldef "iota_" as
    "<IMG SRC='_riotabar.gif' WIDTH=6 HEIGHT=19 ALT=' iota_' TITLE='iota_'>";
  althtmldef "iota_" as '<U>&#8489;</U>';
  latexdef "iota_" as
      "\underline{\mathrm{\rotatebox[origin=C]{180}{$\iota$}}}";
htmldef "oF" as
    " <IMG SRC='circ.gif' WIDTH=8 HEIGHT=19 ALT=' o' TITLE='o'>" +
    "<IMG SRC='subf.gif' WIDTH=6 HEIGHT=19 ALT=' F' TITLE='F'>";
  althtmldef "oF" as " &#8728;<SUB>&#x1D453;</SUB> ";
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "oF" as "\circ_f";
htmldef "oR" as
    " <IMG SRC='circ.gif' WIDTH=8 HEIGHT=19 ALT=' o' TITLE='o'>" +
    "<IMG SRC='subr.gif' WIDTH=5 HEIGHT=19 ALT=' R' TITLE='R'>";
  althtmldef "oR" as " &#8728;<SUB>&#x1D45F;</SUB> ";
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "oR" as "\circ_r";
htmldef "1st" as
    "<IMG SRC='_1st.gif' WIDTH=15 HEIGHT=19 ALT=' 1st' TITLE='1st'>";
  althtmldef "1st" as '1<SUP>st</SUP> ';
  latexdef "1st" as "1^{\rm st}";
htmldef "2nd" as
    "<IMG SRC='_2nd.gif' WIDTH=21 HEIGHT=19 ALT=' 2nd' TITLE='2nd'>";
  althtmldef "2nd" as '2<SUP>nd</SUP> ';
  latexdef "2nd" as "2^{\rm nd}";
htmldef "tpos" as "tpos ";
  althtmldef "tpos" as 'tpos ';
  latexdef "tpos" as "{\rm tpos}";
htmldef "N." as "<IMG SRC='caln.gif' WIDTH=17 HEIGHT=19 ALT=' N.' TITLE='N.'>";
  althtmldef "N." as '<I><B>N</B></I>';
  latexdef "N." as "{\cal N}";
htmldef "+N" as
    " <IMG SRC='_pln.gif' WIDTH=22 HEIGHT=19 ALT=' +N' TITLE='+N'> ";
  althtmldef "+N" as ' +<I><SUB><B>N</B></SUB></I> ';
  latexdef "+N" as "+_{\cal N}";
htmldef ".N" as
    " <IMG SRC='_cdn.gif' WIDTH=14 HEIGHT=19 ALT=' .N' TITLE='.N'> ";
  althtmldef ".N" as ' &middot;<I><SUB><B>N</B></SUB></I> ';
  latexdef ".N" as "\cdot_{\cal N}";
htmldef "<N" as
    " <IMG SRC='_ltn.gif' WIDTH=21 HEIGHT=19 ALT=' &lt;N' TITLE='&lt;N'> ";
  althtmldef "<N" as ' &lt;<I><SUB><B>N</B></SUB></I> ';
  latexdef "<N" as "<_{\cal N}";
htmldef "+pQ" as
    " <IMG SRC='_plpq.gif' WIDTH=28 HEIGHT=19 ALT=' +pQ' TITLE='+pQ'> ";
  althtmldef "+pQ" as ' +<I><SUB>p<B>Q</B></SUB></I> ';
  latexdef "+pQ" as "+_{p{\cal Q}}";
htmldef ".pQ" as
    " <IMG SRC='_cdpq.gif' WIDTH=19 HEIGHT=19 ALT=' .pQ' TITLE='.pQ'> ";
  althtmldef ".pQ" as ' &middot;<I><SUB>p<B>Q</B></SUB></I> ';
  latexdef ".pQ" as "\cdot_{p{\cal Q}}";
htmldef "<pQ" as
    " <IMG SRC='_ltpq.gif' WIDTH=27 HEIGHT=19 ALT=' &lt;pQ' TITLE='&lt;pQ'> ";
  althtmldef "<pQ" as ' &lt;<I><SUB>p<B>Q</B></SUB></I> ';
  latexdef "<pQ" as "<_{p{\cal Q}}";
htmldef "~Q" as
    " <IMG SRC='_simq.gif' WIDTH=21 HEIGHT=19 ALT=' ~Q' TITLE='~Q'> ";
  althtmldef "~Q" as ' ~<I><SUB><B>Q</B></SUB></I> ';
  latexdef "~Q" as "\sim_{\cal Q}";
htmldef "Q." as "<IMG SRC='calq.gif' WIDTH=12 HEIGHT=19 ALT=' Q.' TITLE='Q.'>";
  althtmldef "Q." as '<I><B>Q</B></I>';
  latexdef "Q." as "{\cal Q}";
htmldef "1Q" as "<IMG SRC='_1q.gif' WIDTH=16 HEIGHT=19 ALT=' 1Q' TITLE='1Q'>";
  althtmldef "1Q" as '1<I><SUB><B>Q</B></SUB></I>';
  latexdef "1Q" as "1_{\cal Q}";
htmldef "+Q" as
    " <IMG SRC='_plq.gif' WIDTH=21 HEIGHT=19 ALT=' +Q' TITLE='+Q'> ";
  althtmldef "+Q" as ' +<I><SUB><B>Q</B></SUB></I> ';
  latexdef "+Q" as "+_{\cal Q}";
htmldef ".Q" as
    " <IMG SRC='_cdq.gif' WIDTH=13 HEIGHT=19 ALT=' .Q' TITLE='.Q'> ";
  althtmldef ".Q" as ' &middot;<I><SUB><B>Q</B></SUB></I> ';
  latexdef ".Q" as "\cdot_{\cal Q}";
htmldef "*Q" as
    "<IMG SRC='_astq.gif' WIDTH=16 HEIGHT=19 ALT=' *Q' TITLE='*Q'>";
  althtmldef "*Q" as '*<I><SUB><B>Q</B></SUB></I>';
  latexdef "*Q" as "\ast_{\cal Q}";
htmldef "<Q" as
    " <IMG SRC='_ltq.gif' WIDTH=20 HEIGHT=19 ALT=' &lt;Q' TITLE='&lt;Q'> ";
  althtmldef "<Q" as ' &lt;<I><SUB><B>Q</B></SUB></I> ';
  latexdef "<Q" as "<_{\cal Q}";
htmldef "~Q0" as ' ~<I><SUB><B>Q0</B></SUB></I> ';
  althtmldef "~Q0" as ' ~<I><SUB><B>Q0</B></SUB></I> ';
  latexdef "~Q0" as "\sim_{\cal Q0}";
htmldef "Q0." as '<I><B>Q<SUB>0</SUB></B></I>';
  althtmldef "Q0." as '<I><B>Q<SUB>0</SUB></B></I>';
  latexdef "Q0." as "{\cal Q}_0";
htmldef "0Q0" as '0<I><SUB><B>Q0</B></SUB></I>';
  althtmldef "0Q0" as '0<I><SUB><B>Q0</B></SUB></I>';
  latexdef "0Q0" as "0_{\cal Q0}";
htmldef "+Q0" as ' +<I><SUB><B>Q0</B></SUB></I> ';
  althtmldef "+Q0" as ' +<I><SUB><B>Q0</B></SUB></I> ';
  latexdef "+Q0" as "+_{\cal Q0}";
htmldef ".Q0" as ' &middot;<I><SUB><B>Q0</B></SUB></I> ';
  althtmldef ".Q0" as ' &middot;<I><SUB><B>Q0</B></SUB></I> ';
  latexdef ".Q0" as "\cdot_{\cal Q0}";
htmldef "P." as "<IMG SRC='calp.gif' WIDTH=13 HEIGHT=19 ALT=' P.' TITLE='P.'>";
  althtmldef "P." as '<I><B>P</B></I>';
  latexdef "P." as "{\cal P}";
htmldef "1P" as "<IMG SRC='_1p.gif' WIDTH=15 HEIGHT=19 ALT=' 1P' TITLE='1P'>";
  althtmldef "1P" as '1<I><SUB><B>P</B></SUB></I>';
  latexdef "1P" as "1_{\cal P}";
htmldef "+P." as
    " <IMG SRC='_plp.gif' WIDTH=22 HEIGHT=19 ALT=' +P.' TITLE='+P.'> ";
  althtmldef "+P." as ' +<I><SUB><B>P</B></SUB></I> ';
  latexdef "+P." as "+_{\cal P}";
htmldef ".P." as
    " <IMG SRC='_cdp.gif' WIDTH=13 HEIGHT=19 ALT=' .P.' TITLE='.P.'> ";
  althtmldef ".P." as ' &middot;<I><SUB><B>P</B></SUB></I> ';
  latexdef ".P." as "\cdot_{\cal P}";
htmldef "<P" as
    " <IMG SRC='_ltp.gif' WIDTH=19 HEIGHT=19 ALT=' &lt;P' TITLE='&lt;P'> ";
  althtmldef "<P" as '&lt;<I><SUB><B>P</B></SUB></I> ';
  latexdef "<P" as "<_{\cal P}";
htmldef "~R" as
    " <IMG SRC='_simr.gif' WIDTH=23 HEIGHT=19 ALT=' ~R' TITLE='~R'> ";
  althtmldef "~R" as ' ~<I><SUB><B>R</B></SUB></I> ';
  latexdef "~R" as "\sim_{\cal R}";
htmldef "R." as "<IMG SRC='calr.gif' WIDTH=15 HEIGHT=19 ALT=' R.' TITLE='R.'>";
  althtmldef "R." as '<I><B>R</B></I>';
  latexdef "R." as "{\cal R}";
htmldef "0R" as "<IMG SRC='_0r.gif' WIDTH=18 HEIGHT=19 ALT=' 0R' TITLE='0R'>";
  althtmldef "0R" as '0<I><SUB><B>R</B></SUB></I>';
  latexdef "0R" as "0_{\cal R}";
htmldef "1R" as "<IMG SRC='_1cr.gif' WIDTH=16 HEIGHT=19 ALT=' 1R' TITLE='1R'>";
  althtmldef "1R" as '1<I><SUB><B>R</B></SUB></I>';
  latexdef "1R" as "1_{\cal R}";
htmldef "-1R" as
    "<IMG SRC='_m1r.gif' WIDTH=22 HEIGHT=19 ALT=' -1R' TITLE='-1R'>";
  althtmldef "-1R" as '-1<I><SUB><B>R</B></SUB></I>';
  latexdef "-1R" as "-1_{\cal R}";
htmldef "+R" as
    " <IMG SRC='_plr.gif' WIDTH=23 HEIGHT=19 ALT=' +R' TITLE='+R'> ";
  althtmldef "+R" as ' +<I><SUB><B>R</B></SUB></I> ';
  latexdef "+R" as "+_{\cal R}";
htmldef ".R" as
    " <IMG SRC='_cdcr.gif' WIDTH=14 HEIGHT=19 ALT=' .R' TITLE='.R'> ";
  althtmldef ".R" as ' &middot;<I><SUB><B>R</B></SUB></I> ';
  latexdef ".R" as "\cdot_{\cal R}";
htmldef "<R" as
    " <IMG SRC='_ltr.gif' WIDTH=20 HEIGHT=19 ALT=' &lt;R' TITLE='&lt;R'> ";
  althtmldef "<R" as ' &lt;<I><SUB><B>R</B></SUB></I> ';
  latexdef "<R" as "<_{\cal R}";
htmldef "<RR" as
    " <IMG SRC='_ltbbr.gif' WIDTH=20 HEIGHT=19 ALT=' &lt;RR' TITLE='&lt;RR'> ";
  althtmldef "<RR" as ' &lt;<SUB>&#8477;</SUB> ';
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "<RR" as "<_\mathbb{R}";
htmldef "CC" as "<IMG SRC='bbc.gif' WIDTH=12 HEIGHT=19 ALT=' CC' TITLE='CC'>";
  althtmldef "CC" as '&#8450;';
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "CC" as "\mathbb{C}";
htmldef "RR" as "<IMG SRC='bbr.gif' WIDTH=13 HEIGHT=19 ALT=' RR' TITLE='RR'>";
  althtmldef "RR" as '&#8477;';
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "RR" as "\mathbb{R}";
    /*latexdef "" as "_{10}";*/
    /*latexdef "" as "";*/
    /* suppress base 10 suffix */
htmldef "0" as "<IMG SRC='0.gif' WIDTH=8 HEIGHT=19 ALT=' 0' TITLE='0'>";
  althtmldef "0" as '0';
  latexdef "0" as "0";
htmldef "1" as "<IMG SRC='1.gif' WIDTH=7 HEIGHT=19 ALT=' 1' TITLE='1'>";
  althtmldef "1" as '1';
  latexdef "1" as "1";
htmldef "_i" as "<IMG SRC='rmi.gif' WIDTH=4 HEIGHT=19 ALT=' _i' TITLE='_i'>";
  althtmldef "_i" as 'i';
  latexdef "_i" as "{\rm i}";
htmldef "+" as " <IMG SRC='plus.gif' WIDTH=13 HEIGHT=19 ALT=' +' TITLE='+'> ";
  althtmldef "+" as ' + ';
  latexdef "+" as "+";
htmldef "x." as
    " <IMG SRC='cdot.gif' WIDTH=4 HEIGHT=19 ALT=' x.' TITLE='x.'> ";
  althtmldef "x." as ' &middot; '; /* unicode: &#xb7; */
  latexdef "x." as "\cdot";
htmldef "<_" as
    " <IMG SRC='le.gif' WIDTH=11 HEIGHT=19 ALT=' &lt;_' TITLE='&lt;_'> ";
  althtmldef "<_" as ' &le; ';
  latexdef "<_" as "\le";
htmldef "+oo" as " <IMG SRC='_pinf.gif' WIDTH=29 HEIGHT=19 ALT='+oo' " +
    "TITLE='+oo'>";
  althtmldef "+oo" as '+&infin;';
  latexdef "+oo" as "+\infty";
htmldef "-oo" as " <IMG SRC='_minf.gif' WIDTH=24 HEIGHT=19 ALT='-oo' " +
    "TITLE='-oo'>";
  althtmldef "-oo" as '-&infin;';
  latexdef "-oo" as "-\infty";
htmldef "RR*" as "<IMG SRC='_bbrast.gif' WIDTH=18 HEIGHT=19 ALT=' RR*' " +
    "TITLE='RR*'>";
  althtmldef "RR*" as '&#8477;<SUP>*</SUP>';
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "RR*" as "\mathbb{R}^*";
htmldef "<" as
    " <IMG SRC='lt.gif' WIDTH=11 HEIGHT=19 ALT=' &lt;' TITLE='&lt;'> ";
  althtmldef "<" as ' &lt; ';
  latexdef "<" as "<";
htmldef "-" as
    " <IMG SRC='minus.gif' WIDTH=11 HEIGHT=19 ALT=' -' TITLE='-'> ";
  althtmldef "-" as ' &minus; ';
  latexdef "-" as "-";
htmldef "-u" as
    "<IMG SRC='shortminus.gif' WIDTH=8 HEIGHT=19 ALT=' -u' TITLE='-u'>";
    /* use standard minus sign */
  althtmldef "-u" as '-';
  latexdef "-u" as "\textrm{-}"; /* short minus */
    /*latexdef "-u" as "-_u";*/
htmldef "#" as ' # ';
  althtmldef "#" as ' # ';
  latexdef "#" as "\apart";
htmldef "#RR" as ' #<SUB>&#8477;</SUB> ';
  althtmldef "#RR" as ' #<SUB>&#8477;</SUB> ';
  latexdef "#RR" as "\apart_\mathbb{R}";
htmldef "NN" as "<IMG SRC='bbn.gif' WIDTH=12 HEIGHT=19 ALT=' NN' TITLE='NN'>";
  althtmldef "NN" as '&#8469;'; /* &Nopf; */
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "NN" as "\mathbb{N}";
htmldef "NN0" as
    "<IMG SRC='_bbn0.gif' WIDTH=19 HEIGHT=19 ALT=' NN0' TITLE='NN0'>";
  althtmldef "NN0" as '&#8469;<SUB>0</SUB>';
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "NN0" as "\mathbb{N}_0";
htmldef "ZZ" as "<IMG SRC='bbz.gif' WIDTH=11 HEIGHT=19 ALT=' ZZ' TITLE='ZZ'>";
  althtmldef "ZZ" as '&#8484;';
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "ZZ" as "\mathbb{Z}";
htmldef "QQ" as "<IMG SRC='bbq.gif' WIDTH=13 HEIGHT=19 ALT=' QQ' TITLE='QQ'>";
  althtmldef "QQ" as '&#8474;';
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "QQ" as "\mathbb{Q}";
htmldef "RR+" as "<IMG SRC='_bbrplus.gif' WIDTH=20 HEIGHT=19 ALT=' RR+' " +
    "TITLE='RR+'>";
  althtmldef "RR+" as '&#8477;<SUP>+</SUP>';
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "RR+" as "\mathbb{R}^+";
htmldef "2" as "<IMG SRC='2.gif' WIDTH=8 HEIGHT=19 ALT=' 2' TITLE='2'>";
  althtmldef "2" as '2';
  latexdef "2" as "2";
htmldef "3" as "<IMG SRC='3.gif' WIDTH=8 HEIGHT=19 ALT=' 3' TITLE='3'>";
  althtmldef "3" as '3';
  latexdef "3" as "3";
htmldef "4" as "<IMG SRC='4.gif' WIDTH=9 HEIGHT=19 ALT=' 4' TITLE='4'>";
  althtmldef "4" as '4';
  latexdef "4" as "4";
htmldef "5" as "<IMG SRC='5.gif' WIDTH=8 HEIGHT=19 ALT=' 5' TITLE='5'>";
  althtmldef "5" as '5';
  latexdef "5" as "5";
htmldef "6" as "<IMG SRC='6.gif' WIDTH=8 HEIGHT=19 ALT=' 6' TITLE='6'>";
  althtmldef "6" as '6';
  latexdef "6" as "6";
htmldef "7" as "<IMG SRC='7.gif' WIDTH=9 HEIGHT=19 ALT=' 7' TITLE='7'>";
  althtmldef "7" as '7';
  latexdef "7" as "7";
htmldef "8" as "<IMG SRC='8.gif' WIDTH=8 HEIGHT=19 ALT=' 8' TITLE='8'>";
  althtmldef "8" as '8';
  latexdef "8" as "8";
htmldef "9" as "<IMG SRC='9.gif' WIDTH=8 HEIGHT=19 ALT=' 9' TITLE='9'>";
  althtmldef "9" as '9';
  latexdef "9" as "9";
htmldef "10" as "<IMG SRC='_10.gif' WIDTH=14 HEIGHT=19 ALT=' 10' TITLE='10'>";
  althtmldef "10" as '10';
  latexdef "10" as "10";
htmldef ";" as '<FONT COLOR="#808080">;</FONT>';
  althtmldef ";" as '<SPAN CLASS=hidden STYLE="color:gray">;</SPAN>';
  latexdef ";" as "{\rm;}";
htmldef "ZZ>=" as "<IMG SRC='_bbzge.gif' WIDTH=20 HEIGHT=19 " +
    "ALT=' ZZ&gt;=' TITLE='ZZ&gt;='>";
  althtmldef "ZZ>=" as "&#8484;<SUB>&ge;</SUB>";
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "ZZ>=" as "\mathbb{Z}_\ge";
htmldef "-e" as " <IMG SRC='shortminus.gif' WIDTH=8 HEIGHT=19 ALT=' -' " +
    "TITLE='-'><IMG SRC='sube.gif' WIDTH=6 HEIGHT=19 ALT='e' TITLE='e'>";
  althtmldef "-e" as "-<SUB>&#x1D452;</SUB>";
  latexdef "-e" as "\textrm{-}_e";
htmldef "+e" as "<IMG SRC='plus.gif' WIDTH=13 HEIGHT=19 ALT=' +' TITLE='+'>" +
    "<IMG SRC='sube.gif' WIDTH=6 HEIGHT=19 ALT='e' TITLE='e'>";
  althtmldef "+e" as " +<SUB>&#x1D452;</SUB> ";
  latexdef "+e" as "+_e";
htmldef "*e" as "<IMG SRC='cdot.gif' WIDTH=4 HEIGHT=19 ALT=' x' TITLE='x'>" +
    "<IMG SRC='sube.gif' WIDTH=6 HEIGHT=19 ALT='e' TITLE='e'>";
  althtmldef "*e" as  " &middot;<SUB>e</SUB> ";
  latexdef "*e" as "\cdot_e";
htmldef "(,)" as
    "<IMG SRC='_ioo.gif' WIDTH=13 HEIGHT=19 ALT=' (,)' TITLE='(,)'>";
  althtmldef "(,)" as "(,)";
  latexdef "(,)" as "(,)";
htmldef "(,]" as
    "<IMG SRC='_ioc.gif' WIDTH=12 HEIGHT=19 ALT=' (,]' TITLE='(,]'>";
  althtmldef "(,]" as "(,]";
  latexdef "(,]" as "(,]";
htmldef "[,)" as
    "<IMG SRC='_ico.gif' WIDTH=13 HEIGHT=19 ALT=' [,)' TITLE='[,)'>";
  althtmldef "[,)" as "[,)";
  latexdef "[,)" as "[,)";
htmldef "[,]" as
    "<IMG SRC='_icc.gif' WIDTH=12 HEIGHT=19 ALT=' [,]' TITLE='[,]'>";
  althtmldef "[,]" as "[,]";
  latexdef "[,]" as "[,]";
htmldef "..." as "<IMG SRC='ldots.gif' WIDTH=18 HEIGHT=19 " +
    "ALT=' ...' TITLE='...'>";
  althtmldef "..." as "...";
  latexdef "..." as "\ldots";
htmldef "\/_" as
    " <IMG SRC='veebar.gif' WIDTH=9 HEIGHT=19 ALT=' \/_' TITLE='\/_'> ";
  althtmldef "\/_" as " &#8891; ";
    /* 2-Jan-2016 reverted sans-serif */
  latexdef "\/_" as "\veebar";
htmldef "T." as
    " <IMG SRC='top.gif' WIDTH=11 HEIGHT=19 TITLE='T.' ALIGN=TOP> ";
  althtmldef "T." as ' &#x22A4; ';
  latexdef "T." as "\top";
htmldef "F." as
    " <IMG SRC='perp.gif' WIDTH=11 HEIGHT=19 TITLE='F.' ALIGN=TOP> ";
  althtmldef "F." as ' &perp; ';
  latexdef "F." as "\bot";
htmldef "STAB" as "<SMALL>STAB</SMALL> ";
  althtmldef "STAB" as "<SMALL>STAB</SMALL> ";
  latexdef "STAB" as "\mathrm{STAB} ";
htmldef "DECID" as "<SMALL>DECID</SMALL> ";
  althtmldef "DECID" as "<SMALL>DECID</SMALL> ";
  latexdef "DECID" as "\mathrm{DECID} ";

/* htmldef, althtmldef, latexdef for mathboxes */
/* Note the "Mathbox of" instead of "Mathbox for" to make searching easier. */

/* Mathbox of BJ */
htmldef "Delta0" as "&Delta;<sub>0</sub>";
  althtmldef "Delta0" as "&Delta;<sub>0</sub>";
  latexdef "Delta0" as "\Delta_0 ";
htmldef "Bdd" as "<SMALL>BOUNDED</SMALL> ";
  althtmldef "Bdd" as "<SMALL>BOUNDED</SMALL> ";
  latexdef "Bdd" as "\normalfont\textsc{bounded}} ";
htmldef "Bdd_" as "<SMALL><U>BOUNDED</U></SMALL> ";
  althtmldef "Bdd_" as "<SMALL><U>BOUNDED</U></SMALL> ";
  latexdef "Bdd_" as "\normalfont\textsc{\underline{bounded}}} ";
htmldef "Ind" as "Ind ";
  althtmldef "Ind" as "Ind ";
  latexdef "Ind" as "\mathrm{Ind} ";
/* End of BJ's mathbox */

/* Mathbox of David A. Wheeler */
htmldef "A!" as
  "<IMG SRC='forall.gif' WIDTH=10 HEIGHT=19 ALT=' A.' TITLE='A.'>!";
  althtmldef "A!" as '&forall;!'; /* &#8704; */
  latexdef "A!" as "\forall !";
/* End of David A. Wheeler's mathbox */

/* End of typesetting definition section */
$)
