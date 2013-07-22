package cz.cuni.mff.d3s.spl.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class StringUtilsTest {

	private List<String> oneMemberList;
	private List<String> twoMemberList;
	private List<String> threeMemberList;
	
	@Before
	public void setUpLists() {
		oneMemberList = new ArrayList<>();
		oneMemberList.add("alpha");
		
		twoMemberList = new ArrayList<>();
		twoMemberList.add("alpha");
		twoMemberList.add("bravo");
		
		threeMemberList = new ArrayList<>();
		threeMemberList.add("alpha");
		threeMemberList.add("bravo");
		threeMemberList.add("charlie");
	}
	
	private void assertJoin(String expected, List<?> list) {
		assertEquals(expected, StringUtils.join(list));
	}
	
	@Test
	public void emptyListJoiningProducesEmptyString() {
		assertJoin("", Collections.EMPTY_LIST);
	}
	
	@Test
	public void testJoiningSingleMemberList() {
		assertJoin("alpha", oneMemberList);
	}
	
	@Test
	public void testJoiningTwoMemberList() {
		assertJoin("alpha,bravo", twoMemberList);
	}
	
	@Test
	public void testJoiningThreeMemberList() {
		assertJoin("alpha,bravo,charlie", threeMemberList);
	}
}
