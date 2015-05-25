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

package com.ushahidi.android.data.repository;

import com.ushahidi.android.core.entity.User;
import com.ushahidi.android.data.BaseTestCase;
import com.ushahidi.android.data.BuildConfig;
import com.ushahidi.android.data.entity.UserEntity;
import com.ushahidi.android.data.entity.mapper.UserEntityMapper;
import com.ushahidi.android.data.exception.RepositoryError;
import com.ushahidi.android.data.exception.ValidationException;
import com.ushahidi.android.data.repository.datasource.user.UserDataSource;
import com.ushahidi.android.data.repository.datasource.user.UserDataSourceFactory;
import com.ushahidi.android.data.validator.EmailValidator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Tests {@link com.ushahidi.android.data.database.UserDatabaseHelper}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(emulateSdk = 21, reportSdk = 21, constants = BuildConfig.class)
public class UserDataRepositoryTest extends BaseTestCase {

    private static final long DUMMY_ID = 1;

    private static final String DUMMY_EMAIL = "email@example.com";

    private static final String DUMMY_REAL_NAME = "Real Name";

    private static final String DUMMY_USERNAME = "dudebro";

    private static final Date DUMMY_CREATED = new Date(1415718024);

    private static final Date DUMMY_UPDATED = new Date(1415716223);

    private static final long DUMMY_DEPLOYMENT = 1;

    private static final User.Role DUMMY_ROLE = User.Role.USER;

    private static final String DUMMY_INVALID_EMAIL = "myeail@emal";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private UserDataRepository mUserDataRepository;

    @Mock
    private UserEntityMapper mMockUserEntityMapper;

    @Mock
    private UserEntity mMockUserEntity;

    @Mock
    private User mMockUser;

    @Mock
    private UserDataSourceFactory mMockUserDataSourceFactory;

    @Mock
    private UserDataSource mMockUserDataSource;

    @Mock
    private UserDataRepository.AddCallback mMockUserAddCallback;

    @Mock
    private UserDataRepository.UserUpdateCallback mMockUserUpdateCallback;

    @Mock
    private UserDataRepository.UserDeletedCallback mMockUserDeletedCallback;

    @Mock
    private EmailValidator mMockEmailValidator;

    private User mUser;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        clearSingleton(UserDataRepository.class);
        mUserDataRepository = UserDataRepository
            .getInstance(mMockUserDataSourceFactory, mMockUserEntityMapper,
                mMockEmailValidator);

        given(mMockUserDataSourceFactory.createUserApiDataSource()).willReturn(mMockUserDataSource);
        given(mMockUserDataSourceFactory.createUserDatabaseDataSource())
            .willReturn(mMockUserDataSource);

        mUser = new User();
        mUser.setId(DUMMY_ID);
        mUser.setEmail(DUMMY_EMAIL);
        mUser.setRealName(DUMMY_REAL_NAME);
        mUser.setUsername(DUMMY_USERNAME);
        mUser.setCreated(DUMMY_CREATED);
        mUser.setUpdated(DUMMY_UPDATED);
        mUser.setDeployment(DUMMY_DEPLOYMENT);
        mUser.setRole(DUMMY_ROLE);

    }

    @Test
    public void shouldInvalidateConstructorsNullParameters() throws Exception {
        clearSingleton(UserDataRepository.class);
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid null parameter");
        mUserDataRepository = UserDataRepository.getInstance(null, null, null);
    }

    @Test
    public void shouldSuccessfullyAddAUser() throws Exception {

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((UserDataSource.UserEntityAddCallback) invocation
                    .getArguments()[1]).onUserEntityAdded();
                return null;
            }
        }).when(mMockUserDataSource).addUserEntity(any(UserEntity.class),
            any(UserDataSource.UserEntityAddCallback.class));
        given(mMockEmailValidator.isValid(mUser.getEmail())).willReturn(true);
        given(mMockUserEntityMapper.unmap(mUser)).willReturn(mMockUserEntity);

        mUserDataRepository.addUser(mUser, mMockUserAddCallback);

        verify(mMockUserEntityMapper).unmap(mUser);
        verify(mMockUserAddCallback).onAdded();
    }


    @Test
    public void shouldFailToAddAUser() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((UserDataSource.UserEntityAddCallback) invocation
                    .getArguments()[1]).onError(any(Exception.class));
                return null;
            }
        }).when(mMockUserDataSource).addUserEntity(any(UserEntity.class),
            any(UserDataSource.UserEntityAddCallback.class));

        mUserDataRepository.addUser(mMockUser, mMockUserAddCallback);

        verify(mMockUserAddCallback, times(1)).onError(any(RepositoryError.class));
    }

    @Test
    public void shouldSuccessfullyTestForInvalidUserEmailAddress() throws Exception {
        mUser.setUsername(DUMMY_USERNAME);
        mUser.setEmail(DUMMY_INVALID_EMAIL);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((UserDataSource.UserEntityAddCallback) invocation
                    .getArguments()[1]).onError(any(ValidationException.class));
                return null;
            }
        }).when(mMockUserDataSource).addUserEntity(any(UserEntity.class),
            any(UserDataSource.UserEntityAddCallback.class));

        given(mMockEmailValidator.isValid(mUser.getEmail())).willReturn(false);

        mUserDataRepository.addUser(mUser, mMockUserAddCallback);

        verify(mMockUserAddCallback).onError(any(RepositoryError.class));
    }

    @Test
    public void shouldSuccessfullyTestForEmptyOrNullUsername() throws Exception {
        mUser.setUsername(null);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((UserDataSource.UserEntityAddCallback) invocation
                    .getArguments()[1]).onError(any(ValidationException.class));
                return null;
            }
        }).when(mMockUserDataSource).addUserEntity(any(UserEntity.class),
            any(UserDataSource.UserEntityAddCallback.class));

        given(mMockEmailValidator.isValid(mUser.getEmail())).willReturn(true);

        mUserDataRepository.addUser(mUser, mMockUserAddCallback);

        verify(mMockUserAddCallback).onError(any(RepositoryError.class));
    }

    @Test
    public void shouldSuccessfullyUpdateAUser() throws Exception {

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((UserDataSource.UserEntityUpdateCallback) invocation
                    .getArguments()[1]).onUserEntityUpdated();
                return null;
            }
        }).when(mMockUserDataSource).updateUserEntity(any(UserEntity.class),
            any(UserDataSource.UserEntityUpdateCallback.class));
        given(mMockEmailValidator.isValid(mUser.getEmail())).willReturn(true);
        given(mMockUserEntityMapper.unmap(mUser)).willReturn(mMockUserEntity);

        mUserDataRepository.updateUser(mUser, mMockUserUpdateCallback);

        verify(mMockUserEntityMapper).unmap(mUser);
        verify(mMockUserUpdateCallback).onUserUpdated();
    }

    @Test
    public void shouldFailToUpdateAUser() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((UserDataSource.UserEntityUpdateCallback) invocation
                    .getArguments()[1]).onError(any(Exception.class));
                return null;
            }
        }).when(mMockUserDataSource).updateUserEntity(any(UserEntity.class),
            any(UserDataSource.UserEntityUpdateCallback.class));

        mUserDataRepository.updateUser(mMockUser, mMockUserUpdateCallback);

        verify(mMockUserUpdateCallback, times(2)).onError(any(RepositoryError.class));
    }

    @Test
    public void shouldSuccessfullyDeleteAUser() throws Exception {

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((UserDataSource.UserEntityDeletedCallback) invocation
                    .getArguments()[1]).onUserEntityDeleted();
                return null;
            }
        }).when(mMockUserDataSource).deleteUserEntity(any(UserEntity.class),
            any(UserDataSource.UserEntityDeletedCallback.class));

        given(mMockUserEntityMapper.unmap(mUser)).willReturn(mMockUserEntity);

        mUserDataRepository.deleteUser(mUser, mMockUserDeletedCallback);

        verify(mMockUserEntityMapper).unmap(mUser);
        verify(mMockUserDeletedCallback).onUserDeleted();
    }

    @Test
    public void shouldFailToDeleteAUser() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((UserDataSource.UserEntityDeletedCallback) invocation
                    .getArguments()[1]).onError(any(Exception.class));
                return null;
            }
        }).when(mMockUserDataSource).deleteUserEntity(any(UserEntity.class),
            any(UserDataSource.UserEntityDeletedCallback.class));

        mUserDataRepository.deleteUser(mMockUser, mMockUserDeletedCallback);

        verify(mMockUserDeletedCallback).onError(any(RepositoryError.class));
    }

    @Test
    public void shouldGracefullyFailToAddAUserProfileWithNullEmailAddress() throws Exception {
        mUser.setEmail(null);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((UserDataSource.UserEntityAddCallback) invocation
                    .getArguments()[1]).onError(any(ValidationException.class));
                return null;
            }
        }).when(mMockUserDataSource).addUserEntity(any(UserEntity.class),
            any(UserDataSource.UserEntityAddCallback.class));

        //given(mMockEmailValidator.isValid(mockUser.getEmail())).willReturn(true);
        given(mMockUserEntityMapper.unmap(mUser)).willReturn(mMockUserEntity);

        mUserDataRepository.addUser(mUser, mMockUserAddCallback);

        verifyZeroInteractions(mMockUserEntityMapper.unmap(mUser));
        verify(mMockUserAddCallback, times(1)).onError(any(RepositoryError.class));
    }
}
