# Focus Group on AI for Health (FG-AI4H) - Annotation Platform
The Focus Group on Artificial Intelligence for Health (FG-AI4H) was established under ITU-T
SG16 and operated as a partnership of the International Telecommunication Union (ITU) and the
World Health Organization (WHO) to develop a standardized assessment framework for the
evaluation of AI-based methods for health, diagnosis, triage or treatment decisions. Participation in
the FG-AI4H was free and open to all.

As a consequence, ITU, WHO and WIPO agreed to launch a successor collaboration platform in the
form of a global initiative that would enable, facilitate use and promote implementation of AI for
health, expected to start operations in 2nd quarter of 2024. The Global Initiative on AI for Health
(GI-AI4H) is expected to grandfather the work developed by the FG-AI4H and further leverage and
expand the community it developed.

## Introduction
The Open Code Initiative (OCI) of the FG-AI4H developed a running platform with the proof-of-
concept for the assessment and benchmarking of AI for health solutions using the concepts
identified in the various FG-AI4H Deliverables. The OCI developed the digital building blocks of
the FG-AI4H assessment platform, which can support the end-to-end development and assessment
of AI for health algorithms under consideration of regulatory guidelines and the needs of all AI for
health stakeholders.

The platform is intended to help in developing and assessing AI4H products, and to provide
guidance to implementers developing their own applications and it can also be used by other
stakeholders, such as regulatory bodies and medical professionals. OCI&#39;s open code nature aims to
allow reuse in a variety of contexts, as well as for transparency and easier verification.
The OCI contains five main working packages:
- Core package: Provisions the common services to all packages, e.g., storage, authentication
and authorization to access resources.
- Data acquisition and storage package: Provides safe and secure storage of medical data;
serves as an interface for other packages to access this data using the FHIR standard; and
offers data governance that complies with data protection laws. Facilitates data compilation
for many modalities; registers data and metadata; offers a federated data catalogue and data
governance; ingests data; and manages patient consent information.
- Data annotation package: Provides an annotation campaign management API and annotation
interfaces for many modalities; includes collaboration features; develops a network of
annotation experts; and creates notifications of pending annotation tasks. OCI partners
provide annotation tools integrated with the FG-AI4H platform, e.g., AI-based pre-annotation
platform or 3D image annotation user interface.
- Training package: Provides a Privacy Preserving Federated Learning solution enabling
training on datasets that need to stay at their location. This solution guarantees secure sharing
of models and model aggregation.
- Evaluation (audit) package: Offers testing measures and methods for different quality
dimensions including interpretation, bias, uncertainty, and robustness; questionnaires provide
qualitative evaluation addressing ethical and governance considerations.
- Reporting package: A customizable reporting interface that presents the results of the
Evaluation package.

Over 40 developers, regulators, and medical professionals from five continents were involved in the
OCI development during the FG-AI4H lifetime.

To timely deliver resilient software capability the OCI is implementing strategies that focus on
cybersecurity and survivability across the development process. The OCI adopted the DevSecOps
approach, which is a widely-adopted industry best practice that enables the delivery of resilient
software in a short development cycle. The DevSecOps software lifecycle approach creates cross-
functional teams that unify historically disparate evolutions - development (Dev), cybersecurity
(Sec), and operations (Ops). As a unified team, they follow agile design principles and embrace a
culture that recognizes resilient software is only possible at the intersection of quality, stability, and
security.

The platform prototype was tested in different proof-of concepts and a whitepaper on the
assessment platform has been developed. The integration process of Topic Group TG-Symptoms,
TG-Fall, and TG-Snakes with the OCI platform yielded significant enhancements to the Data
Acquisition and Data Annotation packages. Notably, their inputs were instrumental in refining these
packages. The Validation of the Assessment Platform and its associated processes was conducted
with active participation from several TGs (TG- Dermatology, TG- Ophthalmology, TG- Malaria,
TG- Symptoms, TG- Psychology, TG- Snakes, TG-Radiology, TG-Neuro, TG-Outbreaks) during
the trial audits, which main objectives were:
- To facilitate the conduct of trial audit series and to provide a platform for the submission,
discussion and publication of FG-AI4H audit methods and reports under the special collection
titled “Machine Learning for Health: Algorithm Auditing &amp; Quality Control” in the Journal of
Medical Systems (JOMS)
- To provide basic training to FG-AI4H Topic Groups on how to verify and validate the
technical, clinical and regulatory requirements of their ML4H tool by following the audit
workflow over the AI4H Assessment Platform.
- 
The collaboration between the OCI platform and the Topic Groups (TGs) has been mutually
beneficial, extending beyond platform enhancement to include improvements in TGs&#39; Topic
Description Documents (TDDs). Through the process of running trial audits and engaging in
detailed discussions, the OCI experience has enabled TGs to refine their understanding of their
data&#39;s nature and characteristics. This, in turn, has empowered them to describe their datasets in a
standardized and comprehensive manner. By gaining clarity on data attributes and standardizing
their documentation, TGs have enhanced the quality and utility of their TDDs. This collaborative
exchange underscores the synergistic relationship between the OCI platform and the TGs, driving
continuous improvement and innovation across the FG-AI4H ecosystem.
In parallel, the OCI team collaborated with various FG-AI4H Working Groups, especially WG-
DAISAM and WG-Clinal Evaluation, to implement a range of features aimed at enhancing the
platform&#39;s functionality. These features include:
- Data annotation campaign management: This feature facilitates the streamlined management
of data annotation campaigns, allowing for efficient organization and tracking of tasks within
the platform.
- Health Metadata management: Enabling comprehensive management of health metadata
ensures that relevant information is effectively captured and utilized across various aspects of
the platform, enhancing data accuracy and usability.
- FHIR standard implementation for Data acquisition: Integration of the HL7 Fast Healthcare
Interoperability Resources (FHIR) standard for data acquisition ensures compatibility with
existing healthcare systems, promoting seamless data exchange and interoperability.
- Providing an integration layer for data annotation UI tools: This feature establishes a flexible
integration layer, allowing for the seamless incorporation of various data annotation user
interface (UI) tools into the platform, catering to diverse user preferences and requirements.
For example, integration with Visian, a student project from HPI Potsdam, offers a flexible
human-in-the-loop AI solution tailored for effortless utilization of ML models on medical
image data. It also facilitates comfortable project management for data and model versioning,
enhancing overall workflow efficiency and collaboration.
- Collaboration with industry on privacy-preserving encryption: The OCI team has partnered
with leading industry players such as Inpher.io to implement state-of-the-art privacy-
preserving encryption techniques. By leveraging homomorphic encryption, the platform
ensures robust protection of sensitive data through advanced cryptographic methods, fostering
trust and compliance with privacy regulations.

Furthermore, as part of the development process, comprehensive documentation was created and
hosted on GitHub (https://github.com/fg-ai4h). This documentation serves as a valuable resource
for users, providing guidance on platform usage, feature implementation, and troubleshooting.
Feedback from stakeholders was actively solicited and utilized to refine several FG-AI4H
deliverables, ensuring alignment with user needs and preferences.

Particularly interesting from an ITU perspective is that the OCI of ITU/WHO FG-AI4H offers a
large potential for the development of technical standards in the emerging field of AI that are
directly informed by and tested using actual software code. One good example is the
standardization of data annotation processes.

# Contribution & Forking

If you wish to contribute or fork this project, please read the [documentation](./documentation/Contributing.md).
