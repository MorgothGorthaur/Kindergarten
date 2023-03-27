import React, {useEffect, useState} from 'react';
import TeacherService from '../API/TeacherService';
import Loader from "../UI/Loader/Loader";

function Teachers() {
    const [teachers, setTeachers] = useState([]);
    const [loader, setLoader] = useState(true);
    useEffect(() => {
        async function fetchData() {
            const data = await TeacherService.getAll();
            setTeachers(data);
            setLoader(false)
        }

        fetchData();
    }, []);

    return (
        <div style={{alignItems: 'center'}}>
            <h2>Teachers</h2>
            <p>This is a table with all of the teachers</p>
            {teachers && teachers.length !== 0 ? (
                <table style={{width: '100%', margin: 'auto'}}>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Phone</th>
                        <th>Skype</th>
                        <th>Group Name</th>
                    </tr>
                    </thead>
                    <tbody>
                    {teachers.map((teacher) => (
                        <tr>
                            <td>{teacher.name}</td>
                            <td>{teacher.phone}</td>
                            <td>{teacher.skype}</td>
                            <td>{teacher.groupName}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            ) : loader ? (
                <div style={{
                    textAlign: 'center',
                    height: '100vh',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center'
                }}>
                    <Loader/>
                </div>
            ) : (
                <p>Teachers not found</p>
            )}
        </div>
    );
}

export default Teachers;