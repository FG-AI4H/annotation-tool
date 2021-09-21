# Roles
## Annotator
Depending on my task I am everything between a fast learner to an expert in the field. I provide metadata on myself, such as age, experience, etc. I was vetted before getting started and the quality of my diagnostics assessments were evaluated during an onboarding. I am aware of my task, so I know what (task-specific medical calibration) and how (task-specific technical calibration)  to contribute (onboarding). I am always aware of my pending tasks and I know how much time I spent on each of my tasks, so I can anticipate when I will be finished. I know my supervisor, whom I can contact for any task specific non-technical question.     

## Reviewer
I am an extension of the annotator role. The only difference is that I am an expert in the field and that I am not annotating the data from scratch but I have to add, delete and/or modify existing annotations. These annotations may have been provided by one or many  annotators. After my contribution the annotation is unambiguous and may be considered the ground truth. However, if even I cannot make a final call I would contact my supervisor, for clarification of the next steps. 

## Supervisor (Task)
I am an expert in my field. I know the qualifications and the diagnostic quality of my annotators and reviewers. I know better than anyone else what the task is supposed to be. I am responsible for the medical calibration, hence I am the one to be asked for any task-specific medical questions. I am responsible for consistency and quality of the annotations (quality control). I, however,  do not contribute by annotating nor reviewing any existing annotations. Therefore I need to view all annotations done by any annotator and reviewer and I need to understand the history of annotation changes. I may at any point in time contact any annotator or reviewer in order to discuss with him/her his/her decisions and help him/her to achieve best quality annotations. In case I see a degradation of the annotation quality, I may temporarily suspend any annotator or reviewer until the root cause for the degradation is identified.  

## Manager (Campaign)
I set up campaigns. I can add any number of data points to a campaign. These data points may have been subsetted before based on various conditions. I define the medical task and the diagnoses that need to be done, which in consequence may affect the tooling that is available to the annotators and reviewers. I parameterize the task by setting the minimum number of annotations and I assign annotators and reviewers to campaigns. At any time I can see who is doing what. I know how much time each annotator and reviewer spends on the tasks and I can assess their productivity and compare it to other users. I am starting and closing campaigns and know at any time what the status of the progress is. 

## Data steward
I am responsible  for ingestion and provision of the data points. I make sure the data points fit the tasks and are nor corrupted in any way or form.    

## Presets 
I am a preset of annotations. Most likely I am the output of a model and my pre-annotations shall save the annotators time so that overall the annotation process is accelerated (-> e.g. Active Learning and Human in the Loop)

## Data Scientist
 
# Workflows
## Data ingestion
As a user I want to be able to upload data points. I want to review the data and once I have assured data quality I feed it to the data lake for further diagnostics assessment. 

## Campaign management 
As a user I want to be able to set up, start and close campaigns that are parameterized by one or many tasks, one or many annotators/reviewers, the number of minimum annotations required. 

## Task definition
As a user I want to define the medical task at hand. I need text and figures. The task definition informs the ML task (classification, detection, segmentation) and hence the tools the annotaprs need. It should define inclusion and exclusion criteria and capture edge cases too. 

## Onboarding 
As a user I want to get onboarded. I need to understand the technical and medical tasks a priori. 

## Annotations 
As a user I want to provide annotations. There are various types of annotations, among most common are labels, bounding boxes and pixel masks. 

## Reviews
As a user I want to modify annotations. Depending on the annotation type may be slightly different for each task.  

## Quality control 
Before onboarding and during my time as annotator or reviewer I as a user want to conduct quality control checks.  

## Panel decision 
As a user I want to discuss very hard diagnostic decisions with my peers. 

## User management
As a user I want to add, remove and monitor other users. 

## Continuous learning

## Data export
