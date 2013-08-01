/*
 * Copyright 2013 Charles University in Prague
 * Copyright 2013 Vojtech Horky
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.cuni.mff.d3s.spl.probe.instrument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Ignore;

@Ignore
public class Action implements Runnable {
	private List<Integer> list;
	
	public Action() {
		this(111);
	}
	
	public Action(int size) {
		list = new ArrayList<>(size);
		Random rnd = new Random();
		for (int i = 0; i < size; i++) {
			list.add(rnd.nextInt());
		}
	}
	
	public int action(int i) {
		Collections.shuffle(list);
		Collections.sort(list);
		
		return list.get(0);
	}
	
	public int action2(Object ignored, int i) {
		Collections.shuffle(list);
		Collections.sort(list);
		
		return list.get(0);
	}

	@Override
	public void run() {
		action(-1);
	}
}
