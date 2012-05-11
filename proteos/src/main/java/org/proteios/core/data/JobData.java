/*
 $Id: JobData.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2006 Gregory Vincic, Olle Mansson
 Copyright (C) 2007 Gregory Vincic

 This file is part of Proteios.
 Available at http://www.proteios.org/

 Proteios is free software; you can redistribute it and/or modify it
 under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 Proteios is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 02111-1307, USA.
 */
package org.proteios.core.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class holds information about a job.
 * 
 * @author Nicklas
 * @version 2.0
 * @see org.proteios.core.Job
 * @see <a
 *      href="../../../../../../../development/overview/data/plugins.html">Plugin/jobs
 *      overview</a>
 * @base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 * @hibernate.class table="`Jobs`" lazy="false"
 */
public class JobData
		extends OwnedData
		implements NameableData, RemovableData
{
	public JobData()
	{}

	/*
	 * From the NameableData interface
	 * -------------------------------------------
	 */
	private String name;


	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}

	private String description;


	public String getDescription()
	{
		return description;
	}


	public void setDescription(String description)
	{
		this.description = description;
	}

	// -------------------------------------------
	/*
	 * From the RemovableData interface
	 * -------------------------------------------
	 */
	private boolean removed;


	public boolean isRemoved()
	{
		return removed;
	}


	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}

	// -------------------------------------------
	private int type;


	/**
	 * The type of job: 1 = run plugin, 2 = other
	 * 
	 * @hibernate.property column="`type`" type="int" not-null="true"
	 */
	public int getType()
	{
		return type;
	}


	public void setType(int type)
	{
		this.type = type;
	}

	private PluginDefinitionData pluginDefinition;


	/**
	 * The plugin that executes the job.
	 * 
	 * @hibernate.many-to-one column="`plugindefinition_id`" not-null="false"
	 *                        outer-join="false" update="false"
	 */
	public PluginDefinitionData getPluginDefinition()
	{
		return pluginDefinition;
	}


	public void setPluginDefinition(PluginDefinitionData pluginDefinition)
	{
		this.pluginDefinition = pluginDefinition;
	}

	private PluginConfigurationData pluginConfiguration;


	/**
	 * The plugin configuration for the plugin.
	 * 
	 * @hibernate.many-to-one column="`pluginconfiguration_id`" not-null="false"
	 *                        outer-join="false" update="false"
	 */
	public PluginConfigurationData getPluginConfiguration()
	{
		return pluginConfiguration;
	}


	public void setPluginConfiguration(
			PluginConfigurationData pluginConfiguration)
	{
		this.pluginConfiguration = pluginConfiguration;
	}

	private int status;


	/**
	 * Get the status of the job. 1 = waiting, 2 = running, 3 = completed ok, 4 =
	 * error
	 * 
	 * @hibernate.property column="`status`" type="int" not-null="true"
	 */
	public int getStatus()
	{
		return status;
	}


	public void setStatus(int status)
	{
		this.status = status;
	}

	/**
	 * The maximum allowed length of the status message.
	 */
	public static final int MAX_STATUS_MESSAGE_LENGTH = 65535;
	private String statusMessage;


	/**
	 * Get a status message.
	 * 
	 * @hibernate.property column="`status_message`" type="text"
	 *                     not-null="false"
	 */
	public String getStatusMessage()
	{
		return statusMessage;
	}


	public void setStatusMessage(String statusMessage)
	{
		this.statusMessage = statusMessage;
	}

	private int estimatedExecutionTime;


	/**
	 * Get the estimated execution time of the job. 0 = < 1 minute, 1 = 1 - 10
	 * minutes, 2 = < 1 hour, 3 = > 1 hour
	 * 
	 * @hibernate.property column="`execution_time`" type="int" not-null="true"
	 */
	public int getEstimatedExecutionTime()
	{
		return estimatedExecutionTime;
	}


	public void setEstimatedExecutionTime(int estimatedExecutionTime)
	{
		this.estimatedExecutionTime = estimatedExecutionTime;
	}

	private int percentComplete;


	/**
	 * If the job is running, how many percent has been completed.
	 * 
	 * @hibernate.property column="`percent_complete`" type="int"
	 *                     not-null="true"
	 */
	public int getPercentComplete()
	{
		return percentComplete;
	}


	public void setPercentComplete(int percentComplete)
	{
		this.percentComplete = percentComplete;
	}

	private int priority;


	/**
	 * The jobs priority. A lower value means a higher priority.
	 * 
	 * @hibernate.property column="`priority`" type="int" not-null="true"
	 */
	public int getPriority()
	{
		return priority;
	}


	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	private int activeProjectId;


	/**
	 * The ID of the project that should be made active when running this job.
	 * 
	 * @hibernate.property column="`project_id`" type="int" not-null="true"
	 */
	public int getActiveProjectId()
	{
		return activeProjectId;
	}


	public void setActiveProjectId(int activeProjectId)
	{
		this.activeProjectId = activeProjectId;
	}

	private JobData blocker = null;


	/**
	 * @return job that should be done before executing this one null if no
	 *         blocker is specified
	 * @hibernate.many-to-one column="`blockerjob_id`" update="true"
	 *                        not-null="false"
	 */
	public JobData getBlocker()
	{
		return blocker;
	}


	/**
	 * @param blocker job that this job is waiting for
	 */
	public void setBlocker(JobData blocker)
	{
		this.blocker = blocker;
	}

	private Date created;


	/**
	 * Get the date and time the job was created.
	 * 
	 * @hibernate.property column="`created`" type="timestamp" not-null="true"
	 *                     update="false"
	 */
	public Date getCreated()
	{
		return created;
	}


	public void setCreated(Date created)
	{
		this.created = created;
	}

	private Date started;


	/**
	 * Get the date and time the job was started or null if the job hasn't been
	 * started.
	 * 
	 * @hibernate.property column="`started`" type="timestamp" not-null="false"
	 */
	public Date getStarted()
	{
		return started;
	}


	public void setStarted(Date started)
	{
		this.started = started;
	}

	private Date ended;


	/**
	 * Get the date and time the job was ended or null if the job hasn't ended.
	 * 
	 * @hibernate.property column="`ended`" type="timestamp" not-null="false"
	 */
	public Date getEnded()
	{
		return ended;
	}


	public void setEnded(Date ended)
	{
		this.ended = ended;
	}

	/**
	 * The maximum allowed length of the server name.
	 */
	public static final int MAX_SERVER_LENGTH = 255;
	private String server;


	/**
	 * The name of the server where the job is executing.
	 * 
	 * @hibernate.property column="`server`" type="string" length="255"
	 *                     not-null="false"
	 */
	public String getServer()
	{
		return server;
	}


	public void setServer(String server)
	{
		this.server = server;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, ParameterValueData> parameters;


	/**
	 * The parameters for this job.
	 * 
	 * @hibernate.map table="`JobParameters`" lazy="true" cascade="all"
	 * @hibernate.collection-key column="`job_id`"
	 * @hibernate.collection-index column="`name`" type="string" length="255"
	 * @hibernate.collection-many-to-many column="`value_id`"
	 *                                    class="org.proteios.core.data.ParameterValueData"
	 */
	@SuppressWarnings("unchecked")
	public Map<String, ParameterValueData> getParameters()
	{
		if (parameters == null)
			parameters = new HashMap<String, ParameterValueData>();
		return parameters;
	}


	@SuppressWarnings("unchecked")
	void setParameters(Map<String, ParameterValueData> parameters)
	{
		this.parameters = parameters;
	}

	private Set<MessageData> messages;


	/**
	 * This is the inverse end.
	 * 
	 * @see MessageData#getJob()
	 * @hibernate.set lazy="true" inverse="true" cascade="delete"
	 * @hibernate.collection-key column="`job_id`"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.MessageData"
	 */
	Set<MessageData> getMessages()
	{
		return messages;
	}


	void setMessages(Set<MessageData> messages)
	{
		this.messages = messages;
	}
}
