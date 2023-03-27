import {useEffect, useState} from "react";
import {Button, Form} from "react-bootstrap";
import Input from "../UI/Input/Input";
import KidsService from "../API/KidsService";

const ChildForm = ({child, kids, setKids, tokens, setTokens, setShowForm, group, setGroup}) => {
    const [id, setId] = useState('');
    const [name, setName] = useState('');
    const [birthYear, setBirthYear] = useState('');
    useEffect(() => {
        if (child) {
            setId(child.id);
            setName(child.name);
            setBirthYear(child.birthYear);
        }
    }, [child]);

    const add = (e) => {
        e.preventDefault();
        KidsService.add(name, birthYear, tokens, setTokens).then(data => {
            if (data.ok !== false) {
                console.log(data)
                setKids([...kids, data]);
                setGroup({...group, currentSize: group.currentSize + 1});
                setShowForm(false);
            }
        })
    }

    const update = (e) => {
        e.preventDefault();
        KidsService.update(id, name, birthYear, tokens, setTokens).then(data => {
            console.log(data)
            if (data.ok) {
                setKids([...kids.filter(k => k.id !== id), {id: id, name: name, birthYear: birthYear}])
                setShowForm(false);
            }
        })
    }

    return (
        <Form className="form" onSubmit={child ? update : add}>
            <Input type="text" placeholder="name" value={name} onChange={e => setName(e.target.value)}/>
            <Input type="date" placeholder="birth year" value={birthYear} onChange={e => setBirthYear(e.target.value)}/>
            <Button type="submit"> {child ? "update" : "add"} </Button>
        </Form>
    )

}
export default ChildForm;