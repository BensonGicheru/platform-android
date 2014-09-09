/*
 * Copyright (c) 2014 Ushahidi.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program in the file LICENSE-AGPL. If not, see
 * https://www.gnu.org/licenses/agpl-3.0.html
 */

package com.ushahidi.android.data.entity.mapper;

import com.ushahidi.android.core.entity.Deployment;
import com.ushahidi.android.data.entity.DeploymentEntity;

/**
 * Mapper class used to transform {@link com.ushahidi.android.data.entity.DeploymentEntity} to {@link com.ushahidi.android.core.entity.Deployment} in
 * core
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentEntityMapper {

    public DeploymentEntityMapper() {}

    public Deployment transform(DeploymentEntity deploymentEntity) {
        Deployment deployment = null;

        if (deployment != null) {
            deployment = new Deployment();
            deployment.setId(deploymentEntity.getId());
            deployment.setDescription(deploymentEntity.getDescription());
            deployment.setTitle(deploymentEntity.getTitle());
            deployment.setUrl(deploymentEntity.getUrl());
        }

        return deployment;
    }
}
