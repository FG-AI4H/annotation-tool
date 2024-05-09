# Introduction

Data annotation is one of the most dependable factors on model performance, it serves as an important aspect of data quality control on Artificial Intelligence for health. This document is committed to give a general guideline of data annotation specification, including definition, background and goals, framework, standard operating procedure, scenario classifications and corresponding consistency criteria, as well as recommended metadata, etc.

This annotation module is built according to FG-AI4H-DEL5.3 (Proposed Standard for Data Annotation in Health) which specifies general requirements and guidelines for data annotation to ensure consistency and quality of the annotations. It applies to various types of data including, but not limited to, images, text, audio, and video. The standard covers both manual and automated methods of annotation and applies to all stages of the annotation process, including design, creation, quality assurance, and maintenance. The standard does not address specific annotation tasks or health domains.

# Scope
The scope of this annotation tool is to provide a platform for annotators to annotate data points in the context of health. The tool is supporting the following features:
- Annotator registration and login
- [Campaign creation, management, and monitoring](Campaign-management.md)
- [Dataset management](Dataset-management.md)
- [Data point annotation](Data-point-annotation.md)
- Review of annotations
- Quality control checks
- Data export
- User management
- Continuous learning
- Panel decision

# Annotation UI
Annotation UIs are not developed by the FG-AI4H OCI team itself, but are provided by third-party vendors. The FG-AI4H OCI team is responsible for integrating the annotation UIs into the platform and ensuring that they meet the requirements of the FG-AI4H-DEL5.3 standard.

The Annotation Platform is providing an integration layer for data annotation UI tools: This feature establishes a flexible integration layer, allowing for the seamless incorporation of various data annotation user interface (UI) tools into the platform, catering to diverse user preferences and requirements.

For example, integration with Visian, a student project from HPI Potsdam, offers a flexible human-in-the-loop AI solution tailored for effortless utilization of ML models on medical image data. It also facilitates comfortable project management for data and model versioning, enhancing overall workflow efficiency and collaboration.

# Definitions
- **Data Annotation**: The process of labeling data points with metadata to provide context and meaning to the data.
- **Data Point**: A single unit of data that is annotated.
- **Dataset**: A collection of data points that are annotated together.
- **Annotation**: The metadata that is added to a data point.
- **Annotator**: A user who provides annotations.
- **Reviewer**: A user who reviews and modifies existing annotations.
- **Supervisor**: A user who oversees the annotation process and ensures quality and consistency.
- **Manager**: A user who sets up, starts, and closes campaigns, and monitors the progress.
- **Data Steward**: A user who ensures the quality and integrity of the data points.
- **Preset**: A pre-annotation provided to accelerate the annotation process.
- **Data Scientist**: A user who works with the annotated data to develop AI models.
- **User**: A person who interacts with the annotation tool.
- **Campaign**: A set of tasks that need to be annotated by annotators.
- **Task**: A specific annotation task that needs to be completed by annotators.
- **Quality Control**: The process of checking the quality of annotations.
- **Panel Decision**: A discussion among users to make informed decisions on hard diagnostic cases.
- **Continuous Learning**: The process of learning from the annotations to improve the quality of future annotations.
- **Data Export**: The process of exporting annotated data for further analysis or usage.
- **Metadata**: Additional information about the data point or data set that is not part of the annotation.
- **Health Metadata**: Metadata specific to health data that provides additional context to the annotations.
- **FHIR**: Fast Healthcare Interoperability Resources, a standard for exchanging healthcare information electronically.
- **HL7**: Health Level Seven International, a standard for exchanging healthcare information electronically.

Additional definitions about entities can be found in the [Entities](Entities.md).


