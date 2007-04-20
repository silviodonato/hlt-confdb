package confdb.converter.html;

import confdb.converter.Converter;
import confdb.converter.IPathWriter;
import confdb.data.Path;

public class HtmlPathWriter implements IPathWriter {

	

	public String toString( Path path, Converter converter, String indent ) 
	{
		String str = "<tr>";
		for ( int i = 0; i < indent.length(); i++ )
			str += "<td></td>";
		str += "<td>";
		if ( path.isEndPath() )
			str += "endpath ";
		else
			str += "path ";
		str += path.name() + "</td><td align=\"center\">=</td>";
		for ( int i = 0; i < path.entryCount(); i++  )
		{
			str += "<td>" + path.entry(i).name() + "</td>";
		}
		str += "</tr>\n";
		return str;
	}

}
