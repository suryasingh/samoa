package com.yahoo.labs.samoa.learners.classifiers;

/*
 * #%L
 * SAMOA
 * %%
 * Copyright (C) 2013 Yahoo! Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * License
 */

import com.yahoo.labs.samoa.instances.Instances;
import com.yahoo.labs.samoa.learners.Learner;
import com.yahoo.labs.samoa.topology.ProcessingItem;
import com.yahoo.labs.samoa.topology.Stream;
import com.yahoo.labs.samoa.topology.TopologyBuilder;

import com.github.javacliparser.ClassOption;
import com.github.javacliparser.Configurable;
/**
 * 
 * Classifier that contain a single classifier.
 * 
 */
public final class SingleClassifier implements Learner, Configurable {

	private static final long serialVersionUID = 684111382631697031L;
	
	private ProcessingItem learnerPI;
		
	private Stream resultStream;
	
	private Instances dataset;

	public ClassOption learnerOption = new ClassOption("learner", 'l',
			"Classifier to train.", LocalClassifierAdapter.class, "MOALearner");
	
	private TopologyBuilder builder;


	@Override
	public void init(TopologyBuilder builder, Instances dataset){
		this.builder = builder;
		this.dataset = dataset;
		this.setLayout();
	}


	protected void setLayout() {		
		LocalClassifierProcessor learnerP = new LocalClassifierProcessor();
                LocalClassifierAdapter learner = (LocalClassifierAdapter) this.learnerOption.getValue();
                learner.setDataset(this.dataset);
		learnerP.setClassifier(learner);
                
		learnerPI = this.builder.createPi(learnerP, 1);
		resultStream = this.builder.createStream(learnerPI);
		
		learnerP.setOutputStream(resultStream);
	}

	/* (non-Javadoc)
	 * @see samoa.classifiers.Classifier#getInputProcessingItem()
	 */
	@Override
	public ProcessingItem getInputProcessingItem() {
		return learnerPI;
	}
		
	/* (non-Javadoc)
	 * @see samoa.classifiers.Classifier#getResultStream()
	 */
	@Override
	public Stream getResultStream() {
		return resultStream;
	}
}
