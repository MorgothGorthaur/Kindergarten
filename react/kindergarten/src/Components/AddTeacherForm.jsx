import { useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import Input from '../UI/Input/Input';
import TeacherService from "../API/TeacherService";
const AddTeacherForm = () => {
    const [name, setName] = useState('');
    const [phone, setPhone] = useState('');
    const [skype, setSkype] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const add = (event) => {
        event.preventDefault();
        TeacherService.save(name, phone, skype, email, password).then(data => {
            if(data.debugMessage) alert(data.debugMessage);
        })
    };
    return (
        <Form className="form" onSubmit={add}>
            <Input type="text" placeholder="name" value={name} onChange={e => setName(e.target.value)}/>
            <Input type="text" placeholder="phone" value={phone} onChange={e => setPhone(e.target.value)}/>
            <Input type="text" placeholder="skype" value={skype} onChange={e => setSkype(e.target.value)}/>
            <Input type="text" placeholder="email" value={email} onChange={e => setEmail(e.target.value)}/>
            <Input type="password" placeholder="password" value={password} onChange={e => setPassword(e.target.value)}/>
            <Button type="submit"> add </Button>
        </Form>
    );
};
export default AddTeacherForm;