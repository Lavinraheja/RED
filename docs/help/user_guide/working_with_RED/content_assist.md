<html>
<head>
<link href="PLUGINS_ROOT/org.robotframework.ide.eclipse.main.plugin.doc.user/help/style.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<a href="RED/../../../../help/index.html">RED - Robot Editor User Guide</a> &gt; <a href="RED/../../../../help/user_guide/user_guide.html">User guide</a> &gt; <a href="RED/../../../../help/user_guide/working_with_RED.html">Working with RED</a> &gt; 
<h2>Content assistance</h2>
Content assistance is a functionality provided by Eclipse platform extended to understand Robot data model.<br/>
By default it is invoked by pressing CTRL+SPACE key short-cut, but it can be changed in binding keys preferences.<br/> 
It can be also invoked by typing one of auto activation characters defined in preferences.<br/>
<h3>Content assistance mode of working</h3>
Assist window has multiple modes which are cycled by CTRL+SPACE. Next mode type is displayed at the bottom of the window.
<br/><br/><img src="images/content-assist-modes.gif"/><br/><br/>

All proposal containing given input are displayed. Proposals starting with given input are displayed first.
<br/><br/><img src="images/content-assist-search.png"/><br/><br/>

When given input is camel case string (for example: REA, CrBiFi, WaUCr) keywords proposals that match that string are displayed before other matches.
<br/><br/><img src="images/content-assist-camel-case.png"/><br/>
<h3>Content assistance preferences </h3>
Behavior of content assist can be changed at <code><a class="command" href="javascript:executeCommand('org.eclipse.ui.window.preferences(preferencePageId=org.robotframework.ide.eclipse.main.plugin.preferences.editor.assist)')">
Window -> Preferences -> Robot Framework -> Editor -> Content Assist</a></code>.<br/>
<br/><b>Auto insert</b><br/>
If single proposals insertion is enabled, code assistant will choose and insert automatically single proposals.<br/>
<br/><b>Auto activation</b><br/>
When auto activation is enabled, triggers and delay can be specified to automatically show assist window when one of defined characters is typed.<br/>
<br/><b>Keywords</b><br/>
Options in this section allow to:<br/>
<ul>
<li>
Enable keyword propositions from libraries available in red.xml, but not imported in robot file. Library import will be added automatically when such proposal is accepted.
</li>
<li>
Configure library/resource prefix addition strategy. Such prefix can be enabled/disabled or enabled only if keyword conflict exist.
</li>
<li>
Specify "Tab" behavior for automatically added arguments in source view. Cycle between arguments or exit on last argument can be chosen.
</li>
<li>
Choose ambiguous keyword finding strategy used for features like table cell coloring, documentation show action and template arguments proposals.
</li>
</ul>
<br/><br/><img src="images/content-assist-pref.png"/><br/>
</body>
</html>