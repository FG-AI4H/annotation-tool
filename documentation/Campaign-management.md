# Campaign Management

The `Campaign Management` module is located in the `domain/campaign` package. This module is responsible for creating, managing, and monitoring campaigns.

## Overview

Campaigns define a set of annotators, reviewers and supervisors acting on annotation tasks. Datasets are linked to the campaign and used to define the annotation tasks. The `Campaign Management` module provides the necessary functionalities to manage these campaigns effectively.

## Features

### Campaign Creation

Campaigns can be created by the Manager. 

### Campaign Management

Campaigns can be managed by the Manager. This includes starting and closing campaigns, assigning tasks to annotators, and monitoring the progress of the campaign.

### Campaign Monitoring

The progress of the campaigns can be monitored. This includes tracking the completion of tasks, the quality of annotations, and the overall progress of the campaign.

## Classes

### Campaign

The `Campaign` class represents a campaign. It contains information about the campaign, such as the annotators assigned to the campaign, and the status of the campaign.

For technical details, see the [Campaign class](../src/main/java/org/fgai4h/ap/domain/campaign/entity/CampaignEntity.java)