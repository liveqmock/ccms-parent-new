package com.yunat.ccms.channel.support;


import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Substitution;
import org.apache.oro.text.regex.Util;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.exception.CcmsBusinessException;

@Component
public class MarkVariableResolve {
	private static String IMG_REGEX_EXPRESSION = "<img id=\"(\\d+)\" class=\".*?\" src=\".*?\" alt=\"(.*?)\" />";
	private static String SPAN_REGEX_EXPRESSION = "<span id=\"(\\d+)\" class=\".*?\">(.*?)</span>";
	public Pattern messageImgMarkVariable() {
		return messageMarkVariable(IMG_REGEX_EXPRESSION);
	}

	public Pattern messageSpanMarkVariable() {
		return messageMarkVariable(SPAN_REGEX_EXPRESSION);
	}

	public String messageSubstitute(Pattern pattern, String message, String format) {
		PatternMatcher matcher = new Perl5Matcher();
		return Util.substitute(matcher, pattern, new Perl5Substitution(format), message, Util.SUBSTITUTE_ALL);
	}
	
	private Pattern messageMarkVariable(String regexExpression) {
		PatternCompiler compiler = new Perl5Compiler();
		Pattern pattern = null;
		try {
			pattern = compiler.compile(regexExpression);
			return pattern;
		} catch (Exception e) {
			throw new CcmsBusinessException("message mark variable happend exception", e);
		}
	}

}
