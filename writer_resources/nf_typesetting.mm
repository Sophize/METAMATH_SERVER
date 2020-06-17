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

/* Page title, home page link */
htmltitle "New Foundations Explorer";
htmlhome '<A HREF="mmnf.html"><FONT SIZE=-2 FACE=sans-serif>' +
    '<IMG SRC="nf.gif" BORDER=0 ALT=' +
    '"Home" HEIGHT=32 WIDTH=32 ALIGN=MIDDLE STYLE="margin-bottom:0px">' +
    'Home</FONT></A>';
/* Optional file where bibliographic references are kept */
/* If specified, e.g. "mmnf.html", Metamath will hyperlink all strings of the
   form "[rrr]" (where "rrr" has no whitespace) to "mmnf.html#rrr" */
/* A warning will be given if the file "mmnf.html" with the bibliographical
   references is not present.  It is read in order to check correctness of
   the references. */
/* Note: this is also used to determine the home page (rather than
   extracting it from htmlhome) */
htmlbibliography "mmnf.html";

/* Variable color key at the bottom of each proof table */
htmlvarcolor '<FONT COLOR="#0000FF">wff</FONT> '
    + '<FONT COLOR="#FF0000">setvar</FONT> '
    + '<FONT COLOR="#CC33CC">class</FONT>';

/* GIF and Unicode HTML directories - these are used for the GIF version to
   crosslink to the Unicode version and vice-versa */
htmldir "../nfegif/";
althtmldir "../nfeuni/";


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
htmldef "~P" as "<IMG SRC='scrp.gif' WIDTH=16 HEIGHT=19 TITLE='~P' ALIGN=TOP>";
  althtmldef "~P" as '<FONT FACE=sans-serif>&weierp;</FONT>';
  latexdef "~P" as "{\cal P}";
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
htmldef "-/\" as
    " <IMG SRC='barwedge.gif' WIDTH=9 HEIGHT=19 TITLE='-/\' ALIGN=TOP> ";
  althtmldef "-/\" as ' <FONT FACE=sans-serif>&#8892;</FONT> ';
    /*althtmldef "-/\" as " &#8892; "; */ /* too-high font bug in FF */
    /* barwedge, U022BC, alias ISOAMSB barwed, ['nand'] */
  latexdef "-/\" as "\barwedge";
htmldef "hadd" as "hadd";
  althtmldef "hadd" as "hadd";
  latexdef "hadd" as "\mbox{hadd}";
htmldef "cadd" as "cadd";
  althtmldef "cadd" as "cadd";
  latexdef "cadd" as "\mbox{cadd}";
htmldef "A." as
    "<IMG SRC='forall.gif' WIDTH=10 HEIGHT=19 TITLE='A.' ALIGN=TOP>";
  althtmldef "A." as '<FONT FACE=sans-serif>&forall;</FONT>'; /* &#8704; */
  latexdef "A." as "\forall";
htmldef "setvar" as
    "<IMG SRC='_setvar.gif' WIDTH=20 HEIGHT=19 TITLE='setvar' ALIGN=TOP> ";
  althtmldef "setvar" as '<FONT COLOR="#808080">setvar </FONT>';
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
htmldef "E." as
    "<IMG SRC='exists.gif' WIDTH=9 HEIGHT=19 TITLE='E.' ALIGN=TOP>";
  althtmldef "E." as '<FONT FACE=sans-serif>&exist;</FONT>'; /* &#8707; */
    /* Without sans-serif, bad in Opera and way too big in FF3 */
  latexdef "E." as "\exists";
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
htmldef "u" as "<IMG SRC='_u.gif' WIDTH=10 HEIGHT=19 TITLE='u' ALIGN=TOP>";
  althtmldef "u" as '<I><FONT COLOR="#FF0000">u</FONT></I>';
  latexdef "u" as "u";
htmldef "f" as "<IMG SRC='_f.gif' WIDTH=9 HEIGHT=19 TITLE='f' ALIGN=TOP>";
  althtmldef "f" as '<I><FONT COLOR="#FF0000">f</FONT></I>';
  latexdef "f" as "f";
htmldef "g" as "<IMG SRC='_g.gif' WIDTH=9 HEIGHT=19 TITLE='g' ALIGN=TOP>";
  althtmldef "g" as '<I><FONT COLOR="#FF0000">g</FONT></I>';
  latexdef "g" as "g";
htmldef "E!" as "<IMG SRC='_e1.gif' WIDTH=12 HEIGHT=19 TITLE='E!' ALIGN=TOP>";
  althtmldef "E!" as '<FONT FACE=sans-serif>&exist;!</FONT>';
  latexdef "E!" as "\exists{!}";
htmldef "E*" as "<IMG SRC='_em1.gif' WIDTH=15 HEIGHT=19 TITLE='E*' ALIGN=TOP>";
  althtmldef "E*" as '<FONT FACE=sans-serif>&exist;*</FONT>';
  latexdef "E*" as "\exists^\ast";
htmldef "{" as "<IMG SRC='lbrace.gif' WIDTH=6 HEIGHT=19 TITLE='{' ALIGN=TOP>";
  althtmldef "{" as '{'; /* &lcub; */
  latexdef "{" as "\{";
htmldef "|" as " <IMG SRC='vert.gif' WIDTH=3 HEIGHT=19 TITLE='|' ALIGN=TOP> ";
  althtmldef "|" as ' <FONT FACE=sans-serif>&#8739;</FONT> '; /* &vertbar; */
  latexdef "|" as "|";
htmldef "}" as "<IMG SRC='rbrace.gif' WIDTH=6 HEIGHT=19 TITLE='}' ALIGN=TOP>";
  althtmldef "}" as '}'; /* &rcub; */
  latexdef "}" as "\}";
htmldef "F/" as
    "<IMG SRC='finv.gif' WIDTH=9 HEIGHT=19 ALT=' F/' TITLE='F/'>";
  althtmldef "F/" as "&#8498;";
  latexdef "F/" as "\Finv";
htmldef "F/_" as
    "<IMG SRC='_finvbar.gif' WIDTH=9 HEIGHT=19 ALT=' F/_' TITLE='F/_'>";
  althtmldef "F/_" as "<U>&#8498;</U>";
  latexdef "F/_" as "\underline{\Finv}";
htmldef "class" as
    "<IMG SRC='_class.gif' WIDTH=32 HEIGHT=19 TITLE='class' ALIGN=TOP> ";
  althtmldef "class" as '<FONT COLOR="#808080">class </FONT>';
  latexdef "class" as "{\rm class}";
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
htmldef "A" as "<IMG SRC='_ca.gif' WIDTH=11 HEIGHT=19 TITLE='A' ALIGN=TOP>";
  althtmldef "A" as '<I><FONT COLOR="#CC33CC">A</FONT></I>';
  latexdef "A" as "A";
htmldef "B" as "<IMG SRC='_cb.gif' WIDTH=12 HEIGHT=19 TITLE='B' ALIGN=TOP>";
  althtmldef "B" as '<I><FONT COLOR="#CC33CC">B</FONT></I>';
  latexdef "B" as "B";
htmldef "C" as "<IMG SRC='_cc.gif' WIDTH=12 HEIGHT=19 TITLE='C' ALIGN=TOP>";
  althtmldef "C" as '<I><FONT COLOR="#CC33CC">C</FONT></I>';
  latexdef "C" as "C";
htmldef "D" as "<IMG SRC='_cd.gif' WIDTH=12 HEIGHT=19 TITLE='D' ALIGN=TOP>";
  althtmldef "D" as '<I><FONT COLOR="#CC33CC">D</FONT></I>';
  latexdef "D" as "D";
htmldef "P" as "<IMG SRC='_cp.gif' WIDTH=12 HEIGHT=19 TITLE='P' ALIGN=TOP>";
  althtmldef "P" as '<I><FONT COLOR="#CC33CC">P</FONT></I>';
  latexdef "P" as "P";
htmldef "R" as "<IMG SRC='_cr.gif' WIDTH=12 HEIGHT=19 TITLE='R' ALIGN=TOP>";
  althtmldef "R" as '<I><FONT COLOR="#CC33CC">R</FONT></I>';
  latexdef "R" as "R";
htmldef "S" as "<IMG SRC='_cs.gif' WIDTH=11 HEIGHT=19 TITLE='S' ALIGN=TOP>";
  althtmldef "S" as '<I><FONT COLOR="#CC33CC">S</FONT></I>';
  latexdef "S" as "S";
htmldef "T" as "<IMG SRC='_ct.gif' WIDTH=12 HEIGHT=19 TITLE='T' ALIGN=TOP>";
  althtmldef "T" as '<I><FONT COLOR="#CC33CC">T</FONT></I>';
  latexdef "T" as "T";
htmldef "=/=" as
    " <IMG SRC='ne.gif' WIDTH=12 HEIGHT=19 TITLE='=/=' ALIGN=TOP> ";
  althtmldef "=/=" as ' &ne; ';
  latexdef "=/=" as "\ne";
htmldef "e/" as
    " <IMG SRC='notin.gif' WIDTH=10 HEIGHT=19 TITLE='e/' ALIGN=TOP> ";
  althtmldef "e/" as ' <FONT FACE=sans-serif>&notin;</FONT> ';
  latexdef "e/" as "\notin";
htmldef "_V" as "<IMG SRC='rmcv.gif' WIDTH=10 HEIGHT=19 TITLE='_V' ALIGN=TOP>";
  althtmldef "_V" as 'V';
  latexdef "_V" as "{\rm V}";
htmldef "[." as
    "<IMG SRC='_dlbrack.gif' WIDTH=6 HEIGHT=19 ALT=' [.' TITLE='[.'>";
  /* althtmldef "[." as '&#x298F;'; */   /* corner tick */
  /* U+0323 COMBINING DOT BELOW (HTML &#803;) */
  althtmldef "[." as '[&#803;';
  /* \underaccent is in accents package */
  latexdef "[." as "\underaccent{\dot}{[}";
htmldef "]." as
    "<IMG SRC='_drbrack.gif' WIDTH=5 HEIGHT=19 ALT=' ].' TITLE='].'>";
  /* althtmldef "]." as '&#x298E;'; */   /* corner tick */
  althtmldef "]." as ']&#803;';
  latexdef "]." as "\underaccent{\dot}{]}";
htmldef
    "[_" as "<IMG SRC='_ulbrack.gif' WIDTH=6 HEIGHT=19 TITLE='[_' ALIGN=TOP>";
  althtmldef "[_" as '<U>[</U>'; /* &lsqb; */
  latexdef "[_" as "\underline{[}";
htmldef
    "]_" as "<IMG SRC='_urbrack.gif' WIDTH=5 HEIGHT=19 TITLE=']_' ALIGN=TOP>";
  althtmldef "]_" as '<U>]</U>'; /* &rsqb; */
  latexdef "]_" as "\underline{]}";
htmldef "F" as "<IMG SRC='_cf.gif' WIDTH=13 HEIGHT=19 TITLE='F' ALIGN=TOP>";
  althtmldef "F" as '<I><FONT COLOR="#CC33CC">F</FONT></I>';
  latexdef "F" as "F";
htmldef "G" as "<IMG SRC='_cg.gif' WIDTH=12 HEIGHT=19 TITLE='G' ALIGN=TOP>";
  althtmldef "G" as '<I><FONT COLOR="#CC33CC">G</FONT></I>';
  latexdef "G" as "G";
htmldef "C_" as
    " <IMG SRC='subseteq.gif' WIDTH=12 HEIGHT=19 TITLE='C_' ALIGN=TOP> ";
  althtmldef "C_" as ' <FONT FACE=sans-serif>&#8838;</FONT> '; /* &subE; */
  latexdef "C_" as "\subseteq";
    /* 7-Jun-2019 changed gif, unicode and latex def of "C." from subset to
    subsetneq (BJ) */
htmldef "C." as
    " <IMG SRC='subsetneq.gif' WIDTH=12 HEIGHT=19 TITLE='C.' ALIGN=TOP> ";
    /* subset.gif */
  althtmldef "C." as ' &#x228a; '; /* <FONT FACE=sans-serif>&sub;</FONT> */
  latexdef "C." as "\subsetneq"; /* \subset */
htmldef "~" as " &sim; ";
  althtmldef "~" as ' &sim; ';
  latexdef "~" as "\sim";
htmldef "\" as
    " <IMG SRC='setminus.gif' WIDTH=8 HEIGHT=19 TITLE='\' ALIGN=TOP> ";
  althtmldef "\" as ' <FONT FACE=sans-serif>&#8726;</FONT> '; /* &setmn; */
  latexdef "\" as "\setminus";
htmldef "u." as
    " <IMG SRC='cup.gif' WIDTH=10 HEIGHT=19 TITLE='u.' ALIGN=TOP> ";
  althtmldef "u." as ' &cup; ';
  latexdef "u." as "\cup";
htmldef "-i^i" as
    " &ncap ";
  althtmldef "-i^i" as ' &ncap; ';
  latexdef "-i^i" as "\overline{\cap}";
htmldef "i^i" as
    " <IMG SRC='cap.gif' WIDTH=10 HEIGHT=19 TITLE='i^i' ALIGN=TOP> ";
  althtmldef "i^i" as ' &cap; ';
  latexdef "i^i" as "\cap";
htmldef "(/)" as
    "<IMG SRC='varnothing.gif' WIDTH=11 HEIGHT=19 TITLE='(/)' ALIGN=TOP>";
  althtmldef "(/)" as '<FONT FACE=sans-serif>&empty;</FONT>';
    /*althtmldef "(/)" as '&empty;';*/ /* =&#8709 */ /* bad in Opera */
    /*althtmldef "(/)" as '&#8960;';*/
  latexdef "(/)" as "\varnothing";
htmldef "if" as "<IMG SRC='_if.gif' WIDTH=11 HEIGHT=19 TITLE='if' ALIGN=TOP>";
    /*htmldef "ded" as
    "<IMG SRC='_ded.gif' WIDTH=23 HEIGHT=19 TITLE='ded' ALIGN=TOP>";*/
  althtmldef "if" as ' if';
    /*althtmldef "ded" as 'ded';*/
  latexdef "if" as "{\rm if}";
    /*latexdef "ded" as "{\rm ded}";*/
htmldef "," as "<IMG SRC='comma.gif' WIDTH=4 HEIGHT=19 TITLE=',' ALIGN=TOP> ";
  althtmldef "," as ', ';
  latexdef "," as ",";
htmldef "<." as
    "<IMG SRC='langle.gif' WIDTH=4 HEIGHT=19 TITLE='&lt;.' ALIGN=TOP>";
  althtmldef "<." as '<FONT FACE=sans-serif>&lang;</FONT>'; /* &#9001; */
  latexdef "<." as "\langle";
htmldef ">." as
    "<IMG SRC='rangle.gif' WIDTH=4 HEIGHT=19 TITLE='&gt;.' ALIGN=TOP>";
    althtmldef ">." as '<FONT FACE=sans-serif>&rang;</FONT>'; /* &#9002; */
  latexdef ">." as "\rangle";
htmldef "U." as
    "<IMG SRC='bigcup.gif' WIDTH=13 HEIGHT=19 TITLE='U.' ALIGN=TOP>";
  althtmldef "U." as '<FONT SIZE="+1">&cup;</FONT>'; /* &xcup; */
    /* xcup does not render, and #8899 renders as a small bold cup, on
       Mozilla 1.7.3 on Windows XP */
    /*althtmldef "U." as '&#8899;';*/ /* &xcup; */
  latexdef "U." as "\bigcup";
htmldef "|^|" as
    "<IMG SRC='bigcap.gif' WIDTH=13 HEIGHT=19 TITLE='|^|' ALIGN=TOP>";
  althtmldef "|^|" as '<FONT SIZE="+1">&cap;</FONT>'; /* &xcap; */
    /*althtmldef "|^|" as '&#8898;';*/ /* &xcap; */
  latexdef "|^|" as "\bigcap";

htmldef "Q" as "<IMG SRC='_cq.gif' WIDTH=12 HEIGHT=19 TITLE='Q' ALIGN=TOP>";
  althtmldef "Q" as '<I><FONT COLOR="#CC33CC">Q</FONT></I>';
  latexdef "Q" as "Q";
htmldef "t" as "<IMG SRC='_t.gif' WIDTH=7 HEIGHT=19 TITLE='t' ALIGN=TOP>";
  althtmldef "t" as '<I><FONT COLOR="#FF0000">t</FONT></I>';
  latexdef "t" as "t";
htmldef "s" as "<IMG SRC='_s.gif' WIDTH=7 HEIGHT=19 TITLE='s' ALIGN=TOP>";
  althtmldef "s" as '<I><FONT COLOR="#FF0000">s</FONT></I>';
  latexdef "s" as "s";
htmldef "r" as "<IMG SRC='_r.gif' WIDTH=8 HEIGHT=19 TITLE='r' ALIGN=TOP>";
  althtmldef "r" as '<I><FONT COLOR="#FF0000">r</FONT></I>';
  latexdef "r" as "r";
htmldef "a" as "<IMG SRC='_a.gif' WIDTH=9 HEIGHT=19 TITLE='a' ALIGN=TOP>";
  althtmldef "a" as '<I><FONT COLOR="#FF0000">a</FONT></I>';
  latexdef "a" as "a";
htmldef "b" as "<IMG SRC='_b.gif' WIDTH=8 HEIGHT=19 TITLE='b' ALIGN=TOP>";
  althtmldef "b" as '<I><FONT COLOR="#FF0000">b</FONT></I>';
  latexdef "b" as "b";
htmldef "c" as "<IMG SRC='_c.gif' WIDTH=7 HEIGHT=19 TITLE='c' ALIGN=TOP>";
  althtmldef "c" as '<I><FONT COLOR="#FF0000">c</FONT></I>';
  latexdef "c" as "c";
htmldef "d" as "<IMG SRC='_d.gif' WIDTH=9 HEIGHT=19 TITLE='d' ALIGN=TOP>";
  althtmldef "d" as '<I><FONT COLOR="#FF0000">d</FONT></I>';
  latexdef "d" as "d";
htmldef "e" as "<IMG SRC='_e.gif' WIDTH=8 HEIGHT=19 TITLE='e' ALIGN=TOP>";
  althtmldef "e" as '<I><FONT COLOR="#FF0000">e</FONT></I>';
  latexdef "e" as "e";
htmldef "i" as "<IMG SRC='_i.gif' WIDTH=6 HEIGHT=19 TITLE='i' ALIGN=TOP>";
  althtmldef "i" as '<I><FONT COLOR="#FF0000">i</FONT></I>';
  latexdef "i" as "i";
htmldef "j" as "<IMG SRC='_j.gif' WIDTH=7 HEIGHT=19 TITLE='j' ALIGN=TOP>";
  althtmldef "j" as '<I><FONT COLOR="#FF0000">j</FONT></I>';
  latexdef "j" as "j";
htmldef "k" as "<IMG SRC='_k.gif' WIDTH=9 HEIGHT=19 TITLE='k' ALIGN=TOP>";
  althtmldef "k" as '<I><FONT COLOR="#FF0000">k</FONT></I>';
  latexdef "k" as "k";
htmldef "m" as "<IMG SRC='_m.gif' WIDTH=14 HEIGHT=19 TITLE='m' ALIGN=TOP>";
  althtmldef "m" as '<I><FONT COLOR="#FF0000">m</FONT></I>';
  latexdef "m" as "m";
htmldef "n" as "<IMG SRC='_n.gif' WIDTH=10 HEIGHT=19 TITLE='n' ALIGN=TOP>";
  althtmldef "n" as '<I><FONT COLOR="#FF0000">n</FONT></I>';
  latexdef "n" as "n";
htmldef "o" as "<IMG SRC='_o.gif' WIDTH=8 HEIGHT=19 TITLE='o' ALIGN=TOP>";
  althtmldef "o" as '<I><FONT COLOR="#FF0000">o</FONT></I>';
  latexdef "o" as "o";
htmldef "p" as "<IMG SRC='_p.gif' WIDTH=10 HEIGHT=19 TITLE='p' ALIGN=TOP>";
  althtmldef "p" as '<I><FONT COLOR="#FF0000">p</FONT></I>';
  latexdef "p" as "p";
htmldef "q" as "<IMG SRC='_q.gif' WIDTH=8 HEIGHT=19 TITLE='q' ALIGN=TOP>";
  althtmldef "q" as '<I><FONT COLOR="#FF0000">q</FONT></I>';
  latexdef "q" as "q";
htmldef "E" as "<IMG SRC='_ce.gif' WIDTH=13 HEIGHT=19 TITLE='E' ALIGN=TOP>";
  althtmldef "E" as '<I><FONT COLOR="#CC33CC">E</FONT></I>';
  latexdef "E" as "E";
htmldef "I" as "<IMG SRC='_ci.gif' WIDTH=8 HEIGHT=19 TITLE='I' ALIGN=TOP>";
  althtmldef "I" as '<I><FONT COLOR="#CC33CC">I</FONT></I>';
  latexdef "I" as "I";
htmldef "J" as "<IMG SRC='_cj.gif' WIDTH=10 HEIGHT=19 TITLE='J' ALIGN=TOP>";
  althtmldef "J" as '<I><FONT COLOR="#CC33CC">J</FONT></I>';
  latexdef "J" as "J";
htmldef "K" as "<IMG SRC='_ck.gif' WIDTH=14 HEIGHT=19 TITLE='K' ALIGN=TOP>";
  althtmldef "K" as '<I><FONT COLOR="#CC33CC">K</FONT></I>';
  latexdef "K" as "K";
htmldef "L" as "<IMG SRC='_cl.gif' WIDTH=10 HEIGHT=19 TITLE='L' ALIGN=TOP>";
  althtmldef "L" as '<I><FONT COLOR="#CC33CC">L</FONT></I>';
  latexdef "L" as "L";
htmldef "M" as "<IMG SRC='_cm.gif' WIDTH=15 HEIGHT=19 TITLE='M' ALIGN=TOP>";
  althtmldef "M" as '<I><FONT COLOR="#CC33CC">M</FONT></I>';
  latexdef "M" as "M";
htmldef "N" as "<IMG SRC='_cn.gif' WIDTH=14 HEIGHT=19 TITLE='N' ALIGN=TOP>";
  althtmldef "N" as '<I><FONT COLOR="#CC33CC">N</FONT></I>';
  latexdef "N" as "N";
htmldef "O" as "<IMG SRC='_co.gif' WIDTH=12 HEIGHT=19 TITLE='O' ALIGN=TOP>";
  althtmldef "O" as '<I><FONT COLOR="#CC33CC">O</FONT></I>';
  latexdef "O" as "O";
htmldef "U" as "<IMG SRC='_cu.gif' WIDTH=12 HEIGHT=19 TITLE='U' ALIGN=TOP>";
  althtmldef "U" as '<I><FONT COLOR="#CC33CC">U</FONT></I>';
  latexdef "U" as "U";
htmldef "V" as "<IMG SRC='_cv.gif' WIDTH=12 HEIGHT=19 TITLE='V' ALIGN=TOP>";
  althtmldef "V" as '<I><FONT COLOR="#CC33CC">V</FONT></I>';
  latexdef "V" as "V";
htmldef "W" as "<IMG SRC='_cw.gif' WIDTH=16 HEIGHT=19 TITLE='W' ALIGN=TOP>";
  althtmldef "W" as '<I><FONT COLOR="#CC33CC">W</FONT></I>';
  latexdef "W" as "W";
htmldef "X" as "<IMG SRC='_cx.gif' WIDTH=13 HEIGHT=19 TITLE='X' ALIGN=TOP>";
  althtmldef "X" as '<I><FONT COLOR="#CC33CC">X</FONT></I>';
  latexdef "X" as "X";
htmldef "Y" as "<IMG SRC='_cy.gif' WIDTH=12 HEIGHT=19 TITLE='Y' ALIGN=TOP>";
  althtmldef "Y" as '<I><FONT COLOR="#CC33CC">Y</FONT></I>';
  latexdef "Y" as "Y";
htmldef "Z" as "<IMG SRC='_cz.gif' WIDTH=11 HEIGHT=19 TITLE='Z' ALIGN=TOP>";
  althtmldef "Z" as '<I><FONT COLOR="#CC33CC">Z</FONT></I>';
  latexdef "Z" as "Z";

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

htmldef "iota" as
    "<IMG SRC='riota.gif' WIDTH=6 HEIGHT=19 TITLE='iota' ALIGN=TOP>";
  althtmldef "iota" as '&#8489;';
  latexdef "iota" as "\mathrm{\rotatebox[origin=C]{180}{$\iota$}}";
htmldef "h" as "<IMG SRC='_h.gif' WIDTH=10 HEIGHT=19 TITLE='h' ALIGN=TOP>";
  althtmldef "h" as '<I><FONT COLOR="#FF0000">h</FONT></I>';
  latexdef "h" as "h";
htmldef "H" as "<IMG SRC='_ch.gif' WIDTH=14 HEIGHT=19 TITLE='H' ALIGN=TOP>";
  althtmldef "H" as '<I><FONT COLOR="#CC33CC">H</FONT></I>';
  latexdef "H" as "H";

htmldef "X." as
    " <IMG SRC='times.gif' WIDTH=9 HEIGHT=19 TITLE='X.' ALIGN=TOP> ";
  althtmldef "X." as ' &times; ';
  latexdef "X." as "\times";
htmldef "`'" as "<IMG SRC='_cnv.gif' WIDTH=10 HEIGHT=19 TITLE=" + '"' + "`'" +
    '"' + " ALIGN=TOP>";
    /*htmldef "`'" as
      "<IMG SRC='smallsmile.gif' WIDTH=12 HEIGHT=19 TITLE=" +
      '"' + "`'" + '"' + " ALIGN=TOP>";*/
  althtmldef "`'" as '<FONT SIZE="-1"><SUP>&#9697;</SUP></FONT>'; /* or 8995 */
  latexdef "`'" as "{}^{\smallsmile}";
htmldef "dom" as
    "<IMG SRC='_dom.gif' WIDTH=26 HEIGHT=19 TITLE='dom' ALIGN=TOP> ";
  althtmldef "dom" as 'dom ';
  latexdef "dom" as "{\rm dom}";
htmldef "ran" as
    "<IMG SRC='_ran.gif' WIDTH=22 HEIGHT=19 TITLE='ran' ALIGN=TOP> ";
  althtmldef "ran" as 'ran ';
  latexdef "ran" as "{\rm ran}";
htmldef "|`" as " <IMG SRC='restriction.gif' WIDTH=5 HEIGHT=19 TITLE='|`'" +
    " ALIGN=TOP> ";
  althtmldef "|`" as ' <FONT FACE=sans-serif>&#8638;</FONT> '; /* &uharr; */
  latexdef "|`" as "\restriction";
htmldef '"' as "<IMG SRC='backquote.gif' WIDTH=7 HEIGHT=19 TITLE='" + '"' +
    "' ALIGN=TOP>";
  althtmldef '"' as ' &#8220; ';
  latexdef '"' as "``";
htmldef "o." as
    " <IMG SRC='circ.gif' WIDTH=8 HEIGHT=19 TITLE='o.' ALIGN=TOP> ";
  althtmldef "o." as ' <FONT FACE=sans-serif>&#8728;</FONT> ';
  latexdef "o." as "\circ";
htmldef "Rel" as
    "<IMG SRC='_rel.gif' WIDTH=22 HEIGHT=19 TITLE='Rel' ALIGN=TOP> ";
  althtmldef "Rel" as 'Rel ';
  latexdef "Rel" as "{\rm Rel}";
htmldef
    "Fun" as "<IMG SRC='_fun.gif' WIDTH=25 HEIGHT=19 TITLE='Fun' ALIGN=TOP> ";
  althtmldef "Fun" as 'Fun ';
  latexdef "Fun" as "{\rm Fun}";
htmldef "Fn" as
    " <IMG SRC='_fn.gif' WIDTH=17 HEIGHT=19 TITLE='Fn' ALIGN=TOP> ";
  althtmldef "Fn" as ' Fn ';
  latexdef "Fn" as "{\rm Fn}";
htmldef ":" as "<IMG SRC='colon.gif' WIDTH=4 HEIGHT=19 TITLE=':' ALIGN=TOP>";
  althtmldef ":" as ':';
  latexdef ":" as ":";
htmldef "-->" as
  "<IMG SRC='longrightarrow.gif' WIDTH=23 HEIGHT=19 TITLE='--&gt;' ALIGN=TOP>";
  althtmldef "-->" as '&ndash;&rarr;';
    /* &#xAD;&#x2010;&ndash;&mdash;&minus; (possible symbols test) */
  latexdef "-->" as "\longrightarrow";
htmldef "-1-1->" as
    "<IMG SRC='onetoone.gif' WIDTH=23 HEIGHT=19 TITLE='-1-1-&gt;' ALIGN=TOP>";
  althtmldef "-1-1->" as
    '&ndash;<FONT SIZE=-2 FACE=sans-serif>1-1</FONT>&rarr;';
  latexdef "-1-1->" as
    "\raisebox{.5ex}{${\textstyle{\:}_{\mbox{\footnotesize\rm 1" +
    "\tt -\rm 1}}}\atop{\textstyle{" +
    "\longrightarrow}\atop{\textstyle{}^{\mbox{\footnotesize\rm {\ }}}}}$}";
htmldef "-onto->" as
    "<IMG SRC='onto.gif' WIDTH=23 HEIGHT=19 TITLE='-onto-&gt;' ALIGN=TOP>";
  althtmldef "-onto->" as
    '&ndash;<FONT SIZE=-2 FACE=sans-serif>onto</FONT>&rarr;';
  latexdef "-onto->" as
    "\raisebox{.5ex}{${\textstyle{\:}_{\mbox{\footnotesize\rm {\ }}}}" +
    "\atop{\textstyle{" +
    "\longrightarrow}\atop{\textstyle{}^{\mbox{\footnotesize\rm onto}}}}$}";
htmldef "-1-1-onto->" as "<IMG SRC='onetooneonto.gif' WIDTH=23 HEIGHT=19 " +
    "TITLE='-1-1-onto-&gt;' ALIGN=TOP>";
  althtmldef "-1-1-onto->" as '&ndash;<FONT SIZE=-2 '
    + 'FACE=sans-serif>1-1</FONT>-<FONT SIZE=-2 '
    + 'FACE=sans-serif>onto</FONT>&rarr;';
  latexdef "-1-1-onto->" as
    "\raisebox{.5ex}{${\textstyle{\:}_{\mbox{\footnotesize\rm 1" +
    "\tt -\rm 1}}}\atop{\textstyle{" +
    "\longrightarrow}\atop{\textstyle{}^{\mbox{\footnotesize\rm onto}}}}$}";
htmldef "`" as
    "<IMG SRC='backtick.gif' WIDTH=4 HEIGHT=19 TITLE='` ' ALIGN=TOP>";
    /* Above, IE7 _printing_ is corrupted by '`'; use '` ' which works */
  althtmldef "`" as ' &lsquo;';
  latexdef "`" as "`";
htmldef "Isom" as
    " <IMG SRC='_isom.gif' WIDTH=30 HEIGHT=19 TITLE='Isom' ALIGN=TOP> ";
  althtmldef "Isom" as ' Isom ';
  latexdef "Isom" as "{\rm Isom}";
htmldef "|->" as " <IMG SRC='mapsto.gif' WIDTH=15 HEIGHT=19 TITLE='|-&gt;'" +
    " ALIGN=TOP> ";
  althtmldef "|->" as ' <FONT FACE=sans-serif>&#8614;</FONT> ';
  latexdef "|->" as "\mapsto";
htmldef "1st" as
    "<IMG SRC='_1st.gif' WIDTH=15 HEIGHT=19 TITLE='1st' ALIGN=TOP>";
  althtmldef "1st" as '1<SUP>st</SUP> ';
  latexdef "1st" as "1^{\rm st}";
htmldef "2nd" as
    "<IMG SRC='_2nd.gif' WIDTH=21 HEIGHT=19 TITLE='2nd' ALIGN=TOP>";
  althtmldef "2nd" as '2<SUP>nd</SUP> ';
  latexdef "2nd" as "2^{\rm nd}";
htmldef "Swap" as
    "<FONT FACE=sans-serif> Swap </FONT>";
  althtmldef "Swap" as '<FONT FACE=sans-serif> Swap </FONT>';
  latexdef "Swap" as "{\rm Swap}";

htmldef "_E" as
    " <IMG SRC='rmce.gif' WIDTH=9 HEIGHT=19 TITLE='_E' ALIGN=TOP> ";
  althtmldef "_E" as ' E ';
  latexdef "_E" as "{\rm E}";
htmldef "_I" as
    " <IMG SRC='rmci.gif' WIDTH=4 HEIGHT=19 TITLE='_I' ALIGN=TOP> ";
  althtmldef "_I" as ' I ';
  latexdef "_I" as "{\rm I}";

htmldef "U_" as
    "<IMG SRC='_cupbar.gif' WIDTH=13 HEIGHT=19 TITLE='U_' ALIGN=TOP>";
  althtmldef "U_" as '<U><FONT SIZE="+1">&cup;</FONT></U>'; /* &xcup; */
  latexdef "U_" as "\underline{\bigcup}";
htmldef "|^|_" as
    "<IMG SRC='_capbar.gif' WIDTH=13 HEIGHT=19 TITLE='|^|_' ALIGN=TOP>";
  althtmldef "|^|_" as '<U><FONT SIZE="+1">&cap;</FONT></U>'; /* &xcap; */
  latexdef "|^|_" as "\underline{\bigcap}";

htmldef "(+)" as
    " <IMG SRC='oplus.gif' WIDTH=13 HEIGHT=19 TITLE='(+)' ALIGN=TOP> ";
  althtmldef "(+)" as " &#x2295; ";
  latexdef "(+)" as "\oplus";

htmldef "0c" as '0<SUB><I>c</I></SUB>';
  althtmldef "0c" as '0<SUB><I>c</I></SUB>';
  latexdef "0c" as "0_c";
htmldef "1c" as '1<SUB><I>c</I></SUB>';
  althtmldef "1c" as '1<SUB><I>c</I></SUB>';
  latexdef "1c" as "1_c";
htmldef "+c" as
    " <IMG SRC='_plc.gif' WIDTH=18 HEIGHT=19 TITLE='+o' ALIGN=TOP> ";
  althtmldef "+c" as ' +<SUB><I>c</I></SUB> ';
  latexdef "+c" as "+_c";

htmldef "l" as "<IMG SRC='_l.gif' WIDTH=6 HEIGHT=19 TITLE='l' ALIGN=TOP>";
  althtmldef "l" as '<I><FONT COLOR="#FF0000">l</FONT></I>';
  latexdef "l" as "l";

htmldef "Fix" as
    "<IMG SRC='_fix.gif' WIDTH=21 HEIGHT=19 TITLE='Fix' ALIGN=TOP>";
  althtmldef "Fix" as '<FONT FACE=sans-serif> Fix </FONT>';
  latexdef "Fix" as "{\rm Fix}";
htmldef "<<" as
    "<IMG SRC='llangle.gif' WIDTH=6 HEIGHT=19 TITLE='&lt;&lt;' ALIGN=TOP>";
  althtmldef "<<" as "&#10218;";
  latexdef "<<" as "\langle\langle";
htmldef ">>" as
    "<IMG SRC='rrangle.gif' WIDTH=6 HEIGHT=19 TITLE='&gt;&gt;' ALIGN=TOP>";
  althtmldef ">>" as "&#10219;";
  latexdef ">>" as "\rangle\rangle";
htmldef "(x)" as
    " <IMG SRC='otimes.gif' WIDTH=13 HEIGHT=19 TITLE='(x)' ALIGN=TOP> ";
  althtmldef "(x)" as " &#x2297; ";
  latexdef "(x)" as "\otimes";
htmldef "Image" as "Image";
  althtmldef "Image" as "Image";
  latexdef "Image" as "{\rm Image}";

htmldef "Image_k" as "Image<SUB><I>k</I></SUB>";
  althtmldef "Image_k" as "Image<SUB><I>k</I></SUB>";
  latexdef "Image_k" as "{\rm Image}_k";

htmldef "~P1" as
  "<IMG SRC='scrp.gif' WIDTH=16 HEIGHT=19 TITLE='~P' ALIGN=TOP><SUB>1</SUB> ";
  althtmldef "~P1" as '<FONT FACE=sans-serif>&weierp;</FONT><SUB>1</SUB>';
  latexdef "~P1" as "{\cal P}_1";

htmldef "X._k" as
    " <IMG SRC='times.gif' WIDTH=9 HEIGHT=19 TITLE='X.'" +
       "ALIGN=TOP><SUB><I>k</I></SUB> ";
  althtmldef "X._k" as ' &times;<SUB><I>k</I></SUB> ';
  latexdef "X._k" as "\times_k";
htmldef "`'_k" as "<IMG SRC='_cnv.gif' WIDTH=10 HEIGHT=19 TITLE=" + '"' +
    "`'" + '"' + " ALIGN=TOP><SUB><I>k</I></SUB>";
    /*htmldef "`'" as
      "<IMG SRC='smallsmile.gif' WIDTH=12 HEIGHT=19 TITLE=" +
      '"' + "`'" + '"' + " ALIGN=TOP>";*/
  althtmldef "`'_k" as
   '<FONT SIZE="-1"><SUP>&#9697;</SUP></FONT><SUB><I>k</I></SUB>';
  latexdef "`'_k" as "{}^{\smallsmile}_k";
htmldef '"_k' as "<IMG SRC='backquote.gif' WIDTH=7 HEIGHT=19 TITLE='" + '"' +
    "' ALIGN=TOP><SUB><I>k</I></SUB>";
  althtmldef '"_k' as ' &#8220;<SUB><I>k</I></SUB> ';
  latexdef '"_k' as "``_k";
htmldef "o._k" as
    " <IMG SRC='circ.gif' WIDTH=8 HEIGHT=19 TITLE='o.' ALIGN=TOP>" +
      "<SUB><I>k</I></SUB> ";
  althtmldef "o._k" as
  ' <FONT FACE=sans-serif>&#8728;</FONT><SUB><I>k</I></SUB> ';
  latexdef "o._k" as "\circ_k";
htmldef "SI" as
    "<FONT FACE=sans-serif> SI </FONT>";
  althtmldef "SI" as '<FONT FACE=sans-serif> SI </FONT>';
  latexdef "SI" as "{\rm SI}";
htmldef "Clos1" as
    "<FONT FACE=sans-serif> Clos1 </FONT>";
  althtmldef "Clos1" as '<FONT FACE=sans-serif> Clos1 </FONT>';
  latexdef "Clos1" as "{\rm Clos1}";
htmldef "Phi" as
    "<FONT FACE=sans-serif> Phi </FONT>";
  althtmldef "Phi" as '<FONT FACE=sans-serif> Phi </FONT>';
  latexdef "Phi" as "{\rm Phi}";
htmldef "Proj1" as
    "<FONT FACE=sans-serif> Proj1 </FONT>";
  althtmldef "Proj1" as '<FONT FACE=sans-serif> Proj1 </FONT>';
  latexdef "Proj1" as "{\rm Proj1}";
htmldef "Proj2" as
    "<FONT FACE=sans-serif> Proj2 </FONT>";
  althtmldef "Proj2" as '<FONT FACE=sans-serif> Proj2 </FONT>';
  latexdef "Proj2" as "{\rm Proj2}";
htmldef "_S" as
    "<FONT FACE=sans-serif> S </FONT>";
  althtmldef "_S" as '<FONT FACE=sans-serif> S </FONT>';
  latexdef "_S" as "{\rm S}";
htmldef "U.1" as '&xcup;<SUB>1</SUB>';
  althtmldef "U.1" as '&xcup;<SUB>1</SUB>';
  latexdef "U.1" as "\bigcup_1";
htmldef "_I_k" as
    " <IMG SRC='rmci.gif' WIDTH=4 HEIGHT=19 TITLE='_I_k' ALIGN=TOP>" +
    "<SUB><I>k</I></SUB> ";
  althtmldef "_I_k" as ' I<SUB><I>k</I></SUB> ';
  latexdef "_I_k" as "{\rm I}_k";
htmldef "_S_k" as
    " <FONT FACE=sans-serif>S</FONT><SUB><I>k</I></SUB> ";
  althtmldef "_S_k" as
    ' <FONT FACE=sans-serif>S</FONT><SUB><I>k</I></SUB> ';
  latexdef "_S_k" as "{\rm S}_k";
htmldef "Ins2_k" as
    " <FONT FACE=sans-serif>Ins2</FONT><SUB><I>k</I></SUB> ";
  althtmldef "Ins2_k" as
    ' <FONT FACE=sans-serif>Ins2</FONT><SUB><I>k</I></SUB> ';
  latexdef "Ins2_k" as "{\rm Ins2}_k";
htmldef "Ins3_k" as
    " <FONT FACE=sans-serif>Ins3</FONT><SUB><I>k</I></SUB> ";
  althtmldef "Ins3_k" as
    ' <FONT FACE=sans-serif>Ins3</FONT><SUB><I>k</I></SUB> ';
  latexdef "Ins3_k" as "{\rm Ins3}_k";
htmldef "SI_k" as
    " <FONT FACE=sans-serif>SI</FONT><SUB><I>k</I></SUB> ";
  althtmldef "SI_k" as ' <FONT FACE=sans-serif>SI</FONT><SUB><I>k</I></SUB> ';
  latexdef "SI_k" as "{\rm SI}_k";


htmldef "Ins2" as
    " <FONT FACE=sans-serif>Ins2</FONT> ";
  althtmldef "Ins2" as ' <FONT FACE=sans-serif>Ins2</FONT> ';
  latexdef "Ins2" as "{\rm Ins2}";
htmldef "Ins3" as
    " <FONT FACE=sans-serif>Ins3</FONT> ";
  althtmldef "Ins3" as ' <FONT FACE=sans-serif>Ins3</FONT> ';
  latexdef "Ins3" as "{\rm Ins3}";
htmldef "Ins4" as
    " <FONT FACE=sans-serif>Ins4</FONT> ";
  althtmldef "Ins4" as ' <FONT FACE=sans-serif>Ins4</FONT> ';
  latexdef "Ins4" as "{\rm Ins4}";
htmldef "Cup" as
    " <FONT FACE=sans-serif>Cup</FONT> ";
  althtmldef "Cup" as ' <FONT FACE=sans-serif>Cup</FONT> ';
  latexdef "Cup" as "{\rm Cup}";
htmldef "Disj" as
    " <FONT FACE=sans-serif>Disj</FONT> ";
  althtmldef "Disj" as ' <FONT FACE=sans-serif>Disj</FONT> ';
  latexdef "Disj" as "{\rm Disj}";
htmldef "AddC" as
    " <FONT FACE=sans-serif>AddC</FONT> ";
  althtmldef "AddC" as ' <FONT FACE=sans-serif>AddC</FONT> ';
  latexdef "AddC" as "{\rm AddC}";

htmldef "SI_3" as
    " <FONT FACE=sans-serif>SI</FONT><SUB><I>3</I></SUB> ";
  althtmldef "SI_3" as ' <FONT FACE=sans-serif>SI</FONT><SUB><I>3</I></SUB> ';
  latexdef "SI_3" as "{\rm SI}_3";


htmldef "P6" as
    " <FONT FACE=sans-serif>P6</FONT> ";
  althtmldef "P6" as ' <FONT FACE=sans-serif>P6</FONT> ';
  latexdef "P6" as "{\rm P6}";


htmldef "Nn" as
    " <FONT FACE=sans-serif>Nn</FONT> ";
  althtmldef "Nn" as ' <FONT FACE=sans-serif>Nn</FONT> ';
  latexdef "Nn" as "{\rm Nn}";
htmldef "Fin" as
    " <FONT FACE=sans-serif>Fin</FONT> ";
  althtmldef "Fin" as ' <FONT FACE=sans-serif>Fin</FONT> ';
  latexdef "Fin" as "{\rm Fin}";

htmldef "<_[fin]" as
    " <IMG SRC='le.gif' WIDTH=11 HEIGHT=19 ALT='&lt;_' ALIGN=TOP>" +
    "<SUB>fin</SUB> ";
  althtmldef "<_[fin]" as ' &le;<SUB>fin</SUB> ';
  latexdef "<_[fin]" as "{\le}_{\rm fin}";
htmldef "<[fin]" as
    " <IMG SRC='lt.gif' WIDTH=11 HEIGHT=19 ALT='&lt;' ALIGN=TOP>" +
    "<SUB>fin</SUB> ";
  althtmldef "<[fin]" as ' &lt;<SUB>fin</SUB> ';
  latexdef "<[fin]" as "<_{\rm fin}";
htmldef "Nc[fin]" as
    " <FONT FACE=sans-serif>Nc</FONT><SUB>fin</SUB> ";
  althtmldef "Nc[fin]" as ' <FONT FACE=sans-serif>Nc</FONT><SUB>fin</SUB> ';
  latexdef "Nc[fin]" as "{\rm Nc}_{\rm fin}";
htmldef "_T[fin]" as
    " <FONT FACE=sans-serif>T</FONT><SUB>fin</SUB> ";
  althtmldef "_T[fin]" as ' <FONT FACE=sans-serif>T</FONT><SUB>fin</SUB> ';
  latexdef "_T[fin]" as "{\rm T}_{\rm fin}";
htmldef "Even[fin]" as
    " <FONT FACE=sans-serif>Even</FONT><SUB>fin</SUB> ";
  althtmldef "Even[fin]" as
    ' <FONT FACE=sans-serif>Even</FONT><SUB>fin</SUB> ';
  latexdef "Even[fin]" as "{\rm Even}_{\rm fin}";
htmldef "Odd[fin]" as
    " <FONT FACE=sans-serif>Odd</FONT><SUB>fin</SUB> ";
  althtmldef "Odd[fin]" as ' <FONT FACE=sans-serif>Odd</FONT><SUB>fin</SUB> ';
  latexdef "Odd[fin]" as "{\rm Odd}_{\rm fin}";
htmldef "_S[fin]" as
    " <FONT FACE=sans-serif>S</FONT><SUB>fin</SUB> ";
  althtmldef "_S[fin]" as ' <FONT FACE=sans-serif>S</FONT><SUB>fin</SUB> ';
  latexdef "_S[fin]" as "{\rm S}_{\rm fin}";
htmldef "Sp[fin]" as
    " <FONT FACE=sans-serif>Sp</FONT><SUB>fin</SUB> ";
  althtmldef "Sp[fin]" as ' <FONT FACE=sans-serif>Sp</FONT><SUB>fin</SUB> ';
  latexdef "Sp[fin]" as "{\rm Sp}_{\rm fin}";

htmldef "Funs" as
    " <FONT FACE=sans-serif>Funs</FONT> ";
  althtmldef "Funs" as ' <FONT FACE=sans-serif>Funs</FONT> ';
  latexdef "Funs" as "{\rm Funs}";
htmldef "Fns" as
    " <FONT FACE=sans-serif>Fns</FONT> ";
  althtmldef "Fns" as ' <FONT FACE=sans-serif>Fns</FONT> ';
  latexdef "Fns" as "{\rm Fns}";
htmldef "PProd" as
    " <FONT FACE=sans-serif>PProd</FONT> ";
  althtmldef "PProd" as ' <FONT FACE=sans-serif>PProd</FONT> ';
  latexdef "PProd" as "{\rm PProd}";
htmldef "Cross" as
    " <FONT FACE=sans-serif>Cross</FONT> ";
  althtmldef "Cross" as ' <FONT FACE=sans-serif>Cross</FONT> ';
  latexdef "Cross" as "{\rm Cross}";
htmldef "Pw1Fn" as
    " <FONT FACE=sans-serif>Pw1Fn</FONT> ";
  althtmldef "Pw1Fn" as ' <FONT FACE=sans-serif>Pw1Fn</FONT> ';
  latexdef "Pw1Fn" as "{\rm Pw1Fn}";
htmldef "FullFun" as
    " <FONT FACE=sans-serif>FullFun</FONT> ";
  althtmldef "FullFun" as ' <FONT FACE=sans-serif>FullFun</FONT> ';
  latexdef "FullFun" as "{\rm FullFun}";

htmldef "Trans" as
    " <FONT FACE=sans-serif>Trans</FONT> ";
  althtmldef "Trans" as ' <FONT FACE=sans-serif>Trans</FONT> ';
  latexdef "Trans" as "{\rm Trans}";
htmldef "Ref" as
    " <FONT FACE=sans-serif>Ref</FONT> ";
  althtmldef "Ref" as ' <FONT FACE=sans-serif>Ref</FONT> ';
  latexdef "Ref" as "{\rm Ref}";
htmldef "Antisym" as
    " <FONT FACE=sans-serif>Antisym</FONT> ";
  althtmldef "Antisym" as ' <FONT FACE=sans-serif>Antisym</FONT> ';
  latexdef "Antisym" as "{\rm Antisym}";
htmldef "Po" as
    " <FONT FACE=sans-serif>Po</FONT> ";
  althtmldef "Po" as ' <FONT FACE=sans-serif>Po</FONT> ';
  latexdef "Po" as "{\rm Po}";
htmldef "Connex" as
    " <FONT FACE=sans-serif>Connex</FONT> ";
  althtmldef "Connex" as ' <FONT FACE=sans-serif>Connex</FONT> ';
  latexdef "Connex" as "{\rm Connex}";
htmldef "Or" as
    " <FONT FACE=sans-serif>Or</FONT> ";
  althtmldef "Or" as ' <FONT FACE=sans-serif>Or</FONT> ';
  latexdef "Or" as "{\rm Or}";
htmldef "Fr" as
    " <FONT FACE=sans-serif>Fr</FONT> ";
  althtmldef "Fr" as ' <FONT FACE=sans-serif>Fr</FONT> ';
  latexdef "Fr" as "{\rm Fr}";
htmldef "We" as
    " <FONT FACE=sans-serif>We</FONT> ";
  althtmldef "We" as ' <FONT FACE=sans-serif>We</FONT> ';
  latexdef "We" as "{\rm We}";
htmldef "Ext" as
    " <FONT FACE=sans-serif>Ext</FONT> ";
  althtmldef "Ext" as ' <FONT FACE=sans-serif>Ext</FONT> ';
  latexdef "Ext" as "{\rm Ext}";
htmldef "Sym" as
    " <FONT FACE=sans-serif>Sym</FONT> ";
  althtmldef "Sym" as ' <FONT FACE=sans-serif>Sym</FONT> ';
  latexdef "Sym" as "{\rm Sym}";
htmldef "Er" as
    " <FONT FACE=sans-serif>Er</FONT> ";
  althtmldef "Er" as ' <FONT FACE=sans-serif>Er</FONT> ';
  latexdef "Er" as "{\rm Er}";

htmldef "/." as
    "<IMG SRC='diagup.gif' WIDTH=14 HEIGHT=19 TITLE='/.' ALIGN=TOP>";
  althtmldef "/." as ' <B>/</B> ';
  latexdef "/." as "\diagup";

htmldef "~~" as
    " <IMG SRC='approx.gif' WIDTH=13 HEIGHT=19 TITLE='~~' ALIGN=TOP> ";
  althtmldef "~~" as ' &#8776; '; /* &ap; */
  latexdef "~~" as "\approx";

htmldef "^m" as
    " <IMG SRC='_hatm.gif' WIDTH=15 HEIGHT=19 TITLE='^m' ALIGN=TOP> ";
  althtmldef "^m" as ' &uarr;<SUB><I>m</I></SUB> ';
  latexdef "^m" as "\uparrow_m";
htmldef "^pm" as
    " <IMG SRC='_hatpm.gif' WIDTH=21 HEIGHT=19 TITLE='^pm' ALIGN=TOP> ";
  althtmldef "^pm" as ' &uarr;<SUB><I>pm</I></SUB> ';
  latexdef "^pm" as "\uparrow_{pm}";

htmldef "NC" as
    " <FONT FACE=sans-serif>NC</FONT> ";
  althtmldef "NC" as ' <FONT FACE=sans-serif>NC</FONT> ';
  latexdef "NC" as "{\rm NC}";
htmldef "<_c" as
    " <IMG SRC='le.gif' WIDTH=11 HEIGHT=19 ALT='&lt;_' ALIGN=TOP>" +
    "<SUB>c</SUB> ";
  althtmldef "<_c" as ' &le;<SUB>c</SUB> ';
  latexdef "<_c" as "{\le}_c";
htmldef "<c" as
    " <IMG SRC='lt.gif' WIDTH=11 HEIGHT=19 ALT='&lt;' ALIGN=TOP><SUB>c</SUB> ";
  althtmldef "<c" as ' &lt;<SUB>c</SUB> ';
  latexdef "<c" as "<_c ";
htmldef "Nc" as
    " <FONT FACE=sans-serif>Nc</FONT> ";
  althtmldef "Nc" as ' <FONT FACE=sans-serif>Nc</FONT> ';
  latexdef "Nc" as "{\rm Nc}";
htmldef ".c" as ' &middot;<SUB><I>c</I></SUB> ';
  althtmldef ".c" as ' &middot;<SUB><I>c</I></SUB> ';
  latexdef ".c" as "\cdot_c";
htmldef "T_c" as
    " <FONT FACE=sans-serif>T</FONT><SUB>c</SUB> ";
  althtmldef "T_c" as ' <FONT FACE=sans-serif>T</FONT><SUB>c</SUB> ';
  latexdef "T_c" as "{\rm T}_c ";
htmldef "2c" as '2<SUB><I>c</I></SUB>';
  althtmldef "2c" as '2<SUB><I>c</I></SUB>';
  latexdef "2c" as "2_c";
htmldef "3c" as '3<SUB><I>c</I></SUB>';
  althtmldef "3c" as '3<SUB><I>c</I></SUB>';
  latexdef "3c" as "3_c";
htmldef "^c" as ' &uarr;<SUB><I>c</I></SUB> ';
  althtmldef "^c" as ' &uarr;<SUB><I>c</I></SUB> ';
  latexdef "^c" as "\uparrow_c";

htmldef "Sp[ac]" as
    " <FONT FACE=sans-serif>Sp</FONT><SUB>ac</SUB> ";
  althtmldef "Sp[ac]" as ' <FONT FACE=sans-serif>Sp</FONT><SUB>ac</SUB> ';
  latexdef "Sp[ac]" as "{\rm Sp}_{\rm ac}";

htmldef "TcFn" as "TcFn";
  althtmldef "TcFn" as "TcFn";
  latexdef "TcFn" as "{\rm TcFn}";

htmldef "FRec" as " <FONT FACE=sans-serif>FRec</FONT> ";
  althtmldef "FRec" as " <FONT FACE=sans-serif>FRec</FONT> ";
  latexdef "FRec" as "{\rm FRec}";

htmldef "Dom" as " <FONT FACE=sans-serif>Dom</FONT> ";
  althtmldef "Dom" as " <FONT FACE=sans-serif>Dom</FONT> ";
  latexdef "Dom" as "{\rm Dom}";

htmldef "Ran" as " <FONT FACE=sans-serif>Ran</FONT> ";
  althtmldef "Ran" as " <FONT FACE=sans-serif>Ran</FONT> ";
  latexdef "Ran" as "{\rm Ran}";

$)
